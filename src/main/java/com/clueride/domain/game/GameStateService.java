/*
 * Copyright 2016 Jett Marks
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
 * Created by jett on 1/24/16.
 */
package com.clueride.domain.game;

import com.clueride.domain.puzzle.answer.AnswerPost;
import com.clueride.domain.puzzle.answer.AnswerSummary;
import com.clueride.domain.puzzle.state.PuzzleState;

/**
 * Manages an in-memory copy of the State per Team and per User Session.
 */
public interface GameStateService {
    /**
     * Retrieves the current game state for the session from the session.
     * If this session is empty, ask the GameState Service to provide the instance for the outing
     * and cache this in the session before returning to client.
     * @return Active Session's Game State -- either cached, or now placed in cache.
     */
    GameState getActiveSessionGameState();

    /**
     * Given a Team's ID, return the most recently stored State for that Team.
     * @param teamId - Unique Integer identifying the Team whose State to be retrieved.
     * @return - JSON String representing the entire state object.
     */
    String getGameStateByTeam(Integer teamId);
    /**
     * Indicates that an initial game has all players assembled and we're
     * ready to reveal a puzzle at the starting location.
     * @return updated Game State instance.
     */
    GameState updateWithTeamAssembled();

    /**
     * Updates the indicated Outing with an Arrival Event (including the broadcast of the SSE.
     * This is only valid after the first departure from the starting location;
     * it cannot be used to trigger arrival at the start, that's the job for
     * {@link GameStateService#updateWithTeamAssembled()}
     */
    GameState updateOutingStateWithArrival();

    /**
     * Updates the indicated Outing with a Departure Event (including the broadcast of the SSE).
     */
    GameState updateOutingStateWithDeparture();

    /**
     * Update the stored Game State for a team using the given instance.
     * @param clueRideState - State values to be used for the update.
     * @return - JSON object indicating success of update.
     */

//    String updateGameStateByTeam(ClueRideState clueRideState);

    /**
     * For the session's Game, retrieve the current Puzzle State.
     *
     * @return Session's PuzzleState.
     */
    PuzzleState getPuzzleStateForSession();

    /**
     * Posts the given answer against the current session, updates the
     * overall Outing's summary for this puzzle, and returns that summary.
     * @param answerPost Puzzle ID and Answer choice for that puzzle.
     * @return Summary of responses for the puzzle references in the Answer Post.
     */
    AnswerSummary postAnswer(AnswerPost answerPost);

}
