/*
 * Copyright 2019 Jett Marks
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
 * Created by jett on 3/21/19.
 */
package com.clueride.domain.puzzle.state;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of PuzzleStateService.
 */
public class PuzzleStateServiceImpl implements PuzzleStateService {

    private static Map<Integer, Map<Integer, PuzzleState>> puzzleMapPerOuting = new HashMap<>();

    @Override
    public PuzzleState getPuzzleStateByLocationAndOuting(Integer outingId, Integer locationId) {
        return (puzzleMapPerOuting.get(outingId).get(locationId));
    }

    @Override
    public void addPuzzleState(Integer outingId, Integer locationId, PuzzleState puzzleState) {
        if (!puzzleMapPerOuting.containsKey(outingId)) {
            throw new IllegalStateException("Puzzle State not yet opened for outing " + outingId);
        }

        puzzleMapPerOuting.get(outingId).put(locationId, puzzleState);
    }

    @Override
    public void openOuting(Integer outingId) {
        puzzleMapPerOuting.put(outingId, new HashMap<>());
    }

    @Override
    public void closeOuting(Integer outingId) {
        /* We may want to persist the final results. */
        puzzleMapPerOuting.remove(outingId);
    }
}
