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
     *
     * Once we know the course, we're able to tell if a given Arrival event is the last Arrival and
     * thus the completion of the Course and Outing. Course Completion triggers the broadcast of
     * a Game State with completion status.
     */
    GameState updateOutingStateWithArrival();

    /**
     * Updates the indicated Outing with a Departure Event (including the broadcast of the SSE).
     */
    GameState updateOutingStateWithDeparture();

    /**
     * For the session's Game, retrieve the current Puzzle State.
     *
     * @return Session's PuzzleState.
     */
    PuzzleState getPuzzleStateForSession();

    /**
     * Posts the given answer against the current session, updates the
     * overall Outing's summary for this puzzle, and returns that summary.
     * @param answerPost Puzzle ID and AnswerEntity choice for that puzzle.
     * @return Summary of responses for the puzzle references in the AnswerEntity Post.
     */
    AnswerSummary postAnswer(AnswerPost answerPost);

}
