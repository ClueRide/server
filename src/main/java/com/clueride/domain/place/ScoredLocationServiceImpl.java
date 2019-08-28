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
 * Created by jett on 10/8/17.
 */
package com.clueride.domain.place;

import java.util.List;

import javax.inject.Inject;

import com.clueride.domain.location.LocationEntity;
import com.clueride.domain.location.ReadinessLevel;
import com.clueride.domain.puzzle.Puzzle;
import com.clueride.domain.puzzle.PuzzleService;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Implementation of ScoredLocationService.
 */
public class ScoredLocationServiceImpl implements ScoredLocationService {

    private final PuzzleService puzzleService;

    @Inject
    public ScoredLocationServiceImpl(
            PuzzleService puzzleService
    ) {
        this.puzzleService = puzzleService;
    }

    @Override
    public ReadinessLevel calculateReadinessLevel(LocationEntity location) {
                /* Emptiness across all of these makes it a NODE. */
        if (isNullOrEmpty(location.getName())
                && isNullOrEmpty(location.getDescription())
                && location.getFeaturedImage() == null
                && location.getLocationTypeEntity().getId() == 0
                ) {
            return ReadinessLevel.NODE;
        }

        /* Handle anything that could make this a draft. */
        if (isNullOrEmpty(location.getName())
                || isNullOrEmpty(location.getDescription())
                || location.getFeaturedImage() == null
                || location.getLocationTypeEntity().getId() == 0
                ) {
            return ReadinessLevel.DRAFT;
        }

        /* If we're missing the Puzzles, we're just a Place. */
        List<Puzzle> puzzles = puzzleService.getByLocation(location.getId());
        if (puzzles.size() == 0) {
            return ReadinessLevel.PLACE;
        }

        /* If everything else is defined except our Google Place ID, we're an Attraction. */
        if (location.getGooglePlaceId() == null) {
            return ReadinessLevel.ATTRACTION;
        } else {
            return ReadinessLevel.FEATURED;
        }
    }

}
