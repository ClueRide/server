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
 * Created by jett on 2/28/19.
 */
package com.clueride.domain.puzzle.state;

/**
 * Defines operations on a Puzzle's State, particularly the answers.
 */
public interface PuzzleStateService {

    /**
     * Given the outing and the location, retrieve the corresponding puzzleState.
     *
     * @param outingId unique identifier for an outing.
     * @param locationId unique identifier for the Location.
     * @return matching instance of mutable PuzzleState.
     */
    PuzzleState getPuzzleStateByLocationAndOuting(
            Integer outingId,
            Integer locationId
    );

    /**
     * Places an instance of Puzzle State in the map under the given outing and location.
     *
     * @param outingId unique identifier for an outing.
     * @param locationId unique identifier for the Location.
     * @param puzzleState instance of Puzzle State corresponding to the outing and location.
     */
    void addPuzzleState(
            Integer outingId,
            Integer locationId,
            PuzzleState puzzleState
    );

    /**
     * Prepare the map for tracking the puzzles for the given outing ID.
     *
     * @param outingId unique identifier for an outing.
     */
    void openOuting(Integer outingId);

    /**
     * Releases the puzzle states for the given Outing.
     *
     * @param outingId unique identifier for an outing.
     */
    void closeOuting(Integer outingId);
}
