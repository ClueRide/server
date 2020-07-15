/*
 * Copyright 2017 Jett Marks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by jett on 10/22/17.
 */
package com.clueride.domain.puzzle;

import com.clueride.auth.session.ClueRideSession;
import com.clueride.auth.session.ClueRideSessionDto;
import com.clueride.domain.location.LocationEntity;
import com.clueride.domain.location.LocationStore;
import com.clueride.domain.puzzle.answer.AnswerEntity;
import com.clueride.domain.puzzle.answer.AnswerKey;
import org.slf4j.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of the PuzzleService interface.
 */
public class PuzzleServiceImpl implements PuzzleService {
    @Inject
    private Logger LOGGER;

    @Inject
    @SessionScoped
    @ClueRideSession
    private ClueRideSessionDto clueRideSessionDto;

    private final LocationStore locationStore;
    private final PuzzleStore puzzleStore;

    @Inject
    public PuzzleServiceImpl(
            PuzzleStore puzzleStore,
            LocationStore locationStore
    ) {
        this.puzzleStore = puzzleStore;
        this.locationStore = locationStore;
    }

    @Override
    public Puzzle getById(Integer id) {
        return puzzleStore.getPuzzleById(id).build();
    }

    @Override
    public List<Puzzle> getByLocation(Integer locationId) {
        requireNonNull(locationId, "Location ID is required");
        List<Puzzle> puzzles = new ArrayList<>();
        LocationEntity locationEntity = locationStore.getLocationBuilderById(locationId);
        for (PuzzleEntity puzzleEntity : puzzleStore.getPuzzlesForLocation(locationEntity)) {
            try {
                puzzles.add(puzzleEntity.build());
            } catch (IllegalStateException ise) {
                LOGGER.warn("Problem building puzzle from DB record: ", ise);
                // Continue retrieving remaining records
            }
        }
        return puzzles;
    }

    @Override
    public Puzzle addOrUpdate(PuzzleEntity puzzleEntity) {
        LocationEntity locationEntity = locationStore.getLocationBuilderById(puzzleEntity.getLocationId());
        puzzleEntity.withLocationBuilder(locationEntity);
        linkPuzzleToAnswers(puzzleEntity);
        if (puzzleEntity.getId() == null) {
            puzzleStore.addNew(puzzleEntity);
        } else {
            puzzleStore.update(puzzleEntity);
        }
        return puzzleEntity.build();
    }

    private void linkPuzzleToAnswers(PuzzleEntity puzzleEntity) {
        for (AnswerEntity answerEntity : puzzleEntity.getAnswerEntities()) {
            answerEntity.withPuzzleBuilder(puzzleEntity);
        }
    }

    @Override
    public Puzzle getBlankPuzzleForLocation(LocationEntity locationEntity) {
        LOGGER.debug("Creating Blank puzzle for {}", locationEntity.getName());

        List<AnswerEntity> answerEntities = new ArrayList<>();
        answerEntities.add(new AnswerEntity( AnswerKey.A, "" ));
        answerEntities.add(new AnswerEntity( AnswerKey.B, "" ));
        answerEntities.add(new AnswerEntity( AnswerKey.C, "" ));
        answerEntities.add(new AnswerEntity( AnswerKey.D, "" ));
        PuzzleEntity puzzleEntity = PuzzleEntity.builder()
                .withLocationBuilder(locationEntity)
                .withAnswers(answerEntities);
        return puzzleEntity.build();
    }

    @Override
    public List<PuzzleEntity> removeByLocation(LocationEntity locationEntity) {
        List<PuzzleEntity> puzzleEntities = puzzleStore.getPuzzlesForLocation(locationEntity);
        for (PuzzleEntity puzzleEntity: puzzleEntities) {
            puzzleStore.removePuzzle(puzzleEntity);
        }
        return puzzleEntities;
    }

}
