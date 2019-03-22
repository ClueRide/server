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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clueride.auth.session.ClueRideSession;
import com.clueride.auth.session.ClueRideSessionDto;
import com.clueride.domain.course.Course;
import com.clueride.domain.course.CourseService;
import com.clueride.domain.game.ssevent.SSEventService;
import com.clueride.domain.outing.OutingView;
import com.clueride.domain.puzzle.Puzzle;
import com.clueride.domain.puzzle.PuzzleService;
import com.clueride.domain.puzzle.answer.AnswerKey;
import com.clueride.domain.puzzle.answer.AnswerPost;
import com.clueride.domain.puzzle.answer.AnswerSummary;
import com.clueride.domain.puzzle.state.PuzzleState;
import com.clueride.domain.puzzle.state.PuzzleStateService;
import static java.util.Objects.requireNonNull;

/**
 * Implementation of service handling Game State.
 */
public class GameStateServiceImpl implements GameStateService {
    @Inject
    private Logger LOGGER;

    @Inject
    @SessionScoped
    @ClueRideSession
    private ClueRideSessionDto clueRideSessionDto;

    private final SSEventService ssEventService;
    private final PuzzleService puzzleService;
    private final CourseService courseService;
    private final PuzzleStateService puzzleStateService;

    /** Cached copy of GameState TODO: Persist this via a service. */
    private final static Map<Integer, GameState> gameStateMap = new HashMap<>();

    @Inject
    public GameStateServiceImpl(
            SSEventService ssEventService,
            PuzzleService puzzleService,
            CourseService courseService,
            PuzzleStateService puzzleStateService
    ) {
        this.ssEventService = ssEventService;
        this.puzzleService = puzzleService;
        this.courseService = courseService;
        this.puzzleStateService = puzzleStateService;
    }

    /**
     * Retrieves the GameState by Outing ID.
     * If other sessions have advanced the GameState, it will be available in our map.
     * If not, we want to set it to an appropriate state.
     * @return the GameState instance for the Outing.
     */
    @Override
    public GameState getActiveSessionGameState() {
        Integer outingId = clueRideSessionDto.getOutingView().getId();
        /* TODO: What to do if outingId is empty? */
        if (outingId == null) {
            LOGGER.error("Unable to retrieve GameState when there is no Outing");
            return new GameState.Builder().build();
        }

        GameState gameState = gameStateMap.get(outingId);
        if (gameState == null) {
            gameState = new GameState.Builder().build();
            gameStateMap.put(outingId, gameState);
        }
        return gameState;
    }

    @Override
    public String getGameStateByTeam(Integer teamId) {
        LOGGER.info("Requesting Game State for Team ID " + teamId);
        return "";
    }

    @Override
    public PuzzleState getPuzzleStateForSession() {
        OutingView outingView = clueRideSessionDto.getOutingView();
        int outingId = outingView.getId();

        GameState gameState = gameStateMap.get(outingId);
        return puzzleStateService.getPuzzleStateByLocationAndOuting(
                outingId,
                gameState.getLocationId()
        );
    }

    @Override
    public GameState updateWithTeamAssembled() {
        OutingView outingView = clueRideSessionDto.getOutingView();
        int outingId = outingView.getId();
        LOGGER.info("Opening Game State for outing " + outingId);
        Course course = courseService.getById(outingView.getCourseId());
        setupPuzzles(outingId, course);
        clueRideSessionDto.setCourse(course);
        GameState.Builder gameStateBuilder = GameState.Builder.builder()
                .withTeamAssembled(true)
                .bumpLocation(course);

        gameStateBuilder
                .withPuzzleId(
                        puzzleStateService.getPuzzleStateByLocationAndOuting(
                                outingId,
                                gameStateBuilder.getLocationId()
                        ).getPuzzleId()
                );

        GameState gameState = gameStateBuilder.build();

        synchronized (gameStateMap) {
            gameStateMap.put(outingId, gameState);
            ssEventService.sendTeamReadyEvent(outingId, gameState);
        }
        return gameState;
    }

    /**
     * This happens at the conclusion of a travel step.
     *
     * @return updated GameState.
     */
    @Override
    public GameState updateOutingStateWithArrival() {
        int outingId = clueRideSessionDto.getOutingView().getId();
        /* Validate ability to Arrive. */
        if (!gameStateMap.containsKey(outingId)) {
            throw new IllegalStateException("Team hasn't been assembled yet for this Outing.");
        }
        LOGGER.info("Changing Game State for outing " + outingId + " to Arrival");
        GameState gameState = gameStateMap.get(outingId);
        if (!gameState.getRolling()) {
            throw new IllegalStateException("Cannot Arrive if not yet rolling");
        }

        gameState = GameState.Builder.from(gameState)
                .withTeamAssembled(true)
                .withRolling(false)
                .build();

        synchronized (gameStateMap) {
            gameStateMap.put(outingId, gameState);
            ssEventService.sendArrivalEvent(outingId, gameState);
        }
        return gameState;
    }

    /**
     * This happens as we're about to embark on a new step with a new location and
     * its new puzzle (although the puzzle won't be revealed until we arrive at the
     * new location).
     *
     * @return updated GameState.
     */
    @Override
    public GameState updateOutingStateWithDeparture() {
        int outingId = clueRideSessionDto.getOutingView().getId();
        LOGGER.info("Changing Game State for outing " + outingId + " to Departure");
        Course course = clueRideSessionDto.getCourse();
        GameState gameState = gameStateMap.get(outingId);
        if (gameState.getRolling()) {
            throw new IllegalStateException("Cannot depart if still rolling");
        }
        GameState.Builder gameStateBuilder = GameState.Builder.from(gameState);
        gameStateBuilder.withRolling(true)
                .incrementPathIndex(course.getPathIds().size())
                .bumpLocation(course);
        gameStateBuilder.withPuzzleId(
                puzzleStateService.getPuzzleStateByLocationAndOuting(
                        outingId,
                        gameStateBuilder.getLocationId()
                ).getPuzzleId()
        );
        gameState = gameStateBuilder.build();

        synchronized (gameStateMap) {
            gameStateMap.put(outingId, gameState);
            ssEventService.sendDepartureEvent(outingId, gameState);
        }
        return gameState;
    }

    @Override
    public AnswerSummary postAnswer(AnswerPost answerPost) {
        AnswerKey postedAnswerKey = AnswerKey.valueOf(answerPost.getAnswer());
        AnswerSummary answerSummary = new AnswerSummary();
        answerSummary.setPuzzleId(answerPost.getPuzzleId());
        answerSummary.setMyAnswer(postedAnswerKey);

        PuzzleState puzzleState = getPuzzleStateForSession();
        requireNonNull(puzzleState, "Attempting to post answer against no puzzle state");
        answerSummary.setCorrectAnswer(puzzleState.getCorrectAnswer());
        answerSummary.setAnswerMap(puzzleState.postAnswer(postedAnswerKey));

        synchronized (ssEventService) {
            ssEventService.sendAnswerSummaryEvent(
                    clueRideSessionDto.getOutingView().getId(),
                    answerSummary
            );
        }
        return answerSummary;
    }

    /**
     * Initializes the Puzzle State for all puzzles in the given outing by
     * iterating over each location in the course and choosing a puzzle
     * for that location.
     *
     * @param outingId unique Identifier for the outing we're setting up.
     * @param course contains the list of Locations and their puzzle IDs.
     */
    private void setupPuzzles(Integer outingId, Course course) {
        puzzleStateService.openOuting(outingId);
        for (Integer locationId : course.getLocationIds()) {
            puzzleStateService.addPuzzleState(
                    outingId,
                    locationId,
                    new PuzzleState(
                            getPuzzleForLocation(locationId)
                    )
            );
        }
    }

    private Puzzle getPuzzleForLocation(Integer locationId) {
        List<Puzzle> puzzles = puzzleService.getByLocation(locationId);
        Puzzle chosenPuzzle = null;
        if (puzzles.size() == 0) {
            LOGGER.error("No puzzles for this location");
        } else {
            /* Simply picking the first at this time. */
            chosenPuzzle = puzzles.get(0);
        }
        return chosenPuzzle;
    }

}
