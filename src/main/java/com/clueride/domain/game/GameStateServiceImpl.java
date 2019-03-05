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
import com.clueride.domain.puzzle.state.PuzzleState;

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

    /** Cached copy of GameState TODO: Persist this via a service. */
    private final static Map<Integer, GameState> gameStateMap = new HashMap<>();

    @Inject
    public GameStateServiceImpl(
            SSEventService ssEventService,
            PuzzleService puzzleService,
            CourseService courseService
    ) {
        this.ssEventService = ssEventService;
        this.puzzleService = puzzleService;
        this.courseService = courseService;
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
    public GameState updateWithTeamAssembled() {
        OutingView outingView = clueRideSessionDto.getOutingView();
        int outingId = outingView.getId();
        LOGGER.info("Opening Game State for outing " + outingId);
        Course course = courseService.getById(outingView.getCourseId());
        clueRideSessionDto.setCourse(course);
        GameState.Builder gameStateBuilder = GameState.Builder.builder()
                .withTeamAssembled(true)
                .bumpLocation(course);

        setPuzzle(gameStateBuilder);
        GameState gameState = gameStateBuilder.build();

        synchronized (gameStateMap) {
            gameStateMap.put(outingId, gameState);
            ssEventService.sendTeamReadyEvent(outingId, gameState);
        }
        return gameState;
    }

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
        setPuzzle(gameStateBuilder);
        gameState = gameStateBuilder.build();

        synchronized (gameStateMap) {
            gameStateMap.put(outingId, gameState);
            ssEventService.sendDepartureEvent(outingId, gameState);
        }
        return gameState;
    }

    private void setPuzzle(GameState.Builder gameStateBuilder) {
        List<Puzzle> puzzles = puzzleService.getByLocation(gameStateBuilder.getLocationId());
        Puzzle chosenPuzzle = null;
        if (puzzles.size() == 0) {
            LOGGER.error("No puzzles for this location");
        } else {
            /* Simply picking the first at this time. */
            chosenPuzzle = puzzles.get(0);
            gameStateBuilder.withPuzzleId(chosenPuzzle.getId());
        }

        PuzzleState puzzleState = new PuzzleState(chosenPuzzle);
        clueRideSessionDto.setPuzzleState(puzzleState);
    }

}
