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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.clueride.domain.location.LocationBuilder;
import com.clueride.domain.location.LocationStore;
import com.clueride.domain.puzzle.answer.Answer;
import com.clueride.domain.puzzle.answer.AnswerKey;

/**
 * Implementation of the PuzzleService interface.
 */
public class PuzzleServiceImpl implements PuzzleService {
    @Inject
    private Logger LOGGER;

    private final PuzzleStore puzzleStore;
    private final LocationStore locationStore;

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
        List<Puzzle> puzzles = new ArrayList<>();
        LocationBuilder locationBuilder = LocationBuilder.builder().withId(locationId);
        for (PuzzleBuilder puzzleBuilder : puzzleStore.getPuzzlesForLocation(locationBuilder)) {
            try {
                puzzles.add(puzzleBuilder.build());
            } catch (IllegalStateException ise) {
                LOGGER.warn("Problem building puzzle from DB record: ", ise);
                // Continue retrieving remaining records
            }
        }
        return puzzles;
    }

    @Override
    public Puzzle addNew(PuzzleBuilder puzzleBuilder) {
        LocationBuilder locationBuilder = locationStore.getLocationBuilderById(puzzleBuilder.getLocationId());
        puzzleBuilder.withLocationBuilder(locationBuilder);
        linkPuzzleToAnswers(puzzleBuilder);
        if (puzzleBuilder.getId() == null) {
            puzzleStore.addNew(puzzleBuilder);
        } else {
            puzzleStore.update(puzzleBuilder);
        }
        return puzzleBuilder.build();
    }

    private void linkPuzzleToAnswers(PuzzleBuilder puzzleBuilder) {
        for (Answer answer : puzzleBuilder.getAnswers()) {
            answer.withPuzzleBuilder(puzzleBuilder);
        }
    }

    @Override
    public Puzzle getBlankPuzzleForLocation(LocationBuilder locationBuilder) {
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer( AnswerKey.A, "" ));
        answers.add(new Answer( AnswerKey.B, "" ));
        answers.add(new Answer( AnswerKey.C, "" ));
        answers.add(new Answer( AnswerKey.D, "" ));
        PuzzleBuilder puzzleBuilder = PuzzleBuilder.builder()
                .withLocationBuilder(locationBuilder)
                .withAnswers(answers);
        return puzzleBuilder.build();
    }

}
