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

import com.clueride.auth.session.ClueRideSession;
import com.clueride.auth.session.ClueRideSessionDto;
import com.clueride.domain.badge.event.BadgeEventEntity;
import com.clueride.domain.badge.event.BadgeEventService;
import com.clueride.domain.course.Course;
import com.clueride.domain.course.CourseService;
import com.clueride.domain.game.ssevent.SSEventService;
import com.clueride.domain.location.Location;
import com.clueride.domain.location.LocationService;
import com.clueride.domain.outing.NoSessionOutingException;
import com.clueride.domain.outing.OutingConstants;
import com.clueride.domain.outing.OutingService;
import com.clueride.domain.outing.OutingView;
import com.clueride.domain.puzzle.Puzzle;
import com.clueride.domain.puzzle.PuzzleService;
import com.clueride.domain.puzzle.answer.AnswerKey;
import com.clueride.domain.puzzle.answer.AnswerPost;
import com.clueride.domain.puzzle.answer.AnswerSummary;
import com.clueride.domain.puzzle.state.PuzzleState;
import com.clueride.domain.puzzle.state.PuzzleStateService;
import org.slf4j.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final BadgeEventService badgeEventService;
    private final CourseService courseService;
    private final LocationService locationService;
    private final OutingService outingService;
    private final PuzzleService puzzleService;
    private final PuzzleStateService puzzleStateService;
    private final SSEventService ssEventService;

    /** Cached copy of GameState. */
    private final static Map<Integer, GameState.Builder> gameStateMap = new HashMap<>();

    @Inject
    public GameStateServiceImpl(
            BadgeEventService badgeEventService,
            CourseService courseService,
            LocationService locationService,
            OutingService outingService,
            PuzzleService puzzleService,
            PuzzleStateService puzzleStateService,
            SSEventService ssEventService
    ) {
        this.badgeEventService = badgeEventService;
        this.courseService = courseService;
        this.locationService = locationService;
        this.outingService = outingService;
        this.puzzleService = puzzleService;
        this.puzzleStateService = puzzleStateService;
        this.ssEventService = ssEventService;
    }

    /**
     * Retrieves the GameState by Outing ID.
     *
     * If other sessions have advanced the GameState, it will be available in our map.
     * If not, we want to set it to an appropriate state.
     * If there is no Game in progress, or there is no Outing, the Game State should reflect this.
     *
     * @return the GameState instance for the Outing.
     */
    @Override
    public GameState getActiveSessionGameState() {
        return getBuilderForSession().build();
    }

    private GameState.Builder getBuilderForSession() {
        requireNonNull(clueRideSessionDto, "Session has not been established");

        if (clueRideSessionDto.hasNoOuting()) {
            throw new NoSessionOutingException();
        }

        Integer outingId = clueRideSessionDto.getOutingId();

        GameState.Builder gameStateBuilder = gameStateMap.get(outingId);
        if (gameStateBuilder == null) {
            LOGGER.info("Opening Game State for outing " + outingId);
            gameStateBuilder = new GameState.Builder();
            gameStateMap.put(outingId, gameStateBuilder);
        }
        return gameStateBuilder;
    }

    @Override
    public PuzzleState getPuzzleStateForSession() {
        GameState.Builder gameStateBuilder = getBuilderForSession();
        return puzzleStateService.getPuzzleStateByLocationAndOuting(
                clueRideSessionDto.getOutingId(),
                gameStateBuilder.getLocationId()
        );
    }

    @Override
    public GameState updateWithTeamAssembled() {
        int outingId = clueRideSessionDto.getOutingId();
        OutingView outingView = outingService.getViewById(outingId);
        Course course = courseService.getById(outingView.getCourseId());
        setupPuzzles(outingId, course);
        GameState.Builder gameStateBuilder = getBuilderForSession()
                .withTeamAssembled(true);

        int locationId = updateLocation(gameStateBuilder, course);

        gameStateBuilder.withPuzzleId(
                puzzleStateService.getPuzzleStateByLocationAndOuting(
                        outingId,
                        locationId
                ).getPuzzleId()
        );

        GameState gameState = gameStateBuilder.build();

        synchronized (gameStateMap) {
            gameStateMap.put(outingId, gameStateBuilder);
            ssEventService.sendTeamReadyEvent(outingId, gameState);
        }
        recordTeamBadgeEvent("teamAssembled");
        return gameState;
    }

    /**
     * This happens at the conclusion of a travel step.
     *
     * A special case is when we arrive at the end of the Course. In this case,
     * we change the GameState to indicate the Outing is complete.
     *
     * @return updated GameState.
     */
    @Override
    public GameState updateOutingStateWithArrival() {
        GameState.Builder gameStateBuilder = getBuilderForSession();
        /* Validate ability to Arrive. */
        if (!gameStateBuilder.getTeamAssembled()) {
            throw new IllegalStateException("Team hasn't been assembled yet for this Outing.");
        }
        if (!gameStateBuilder.getRolling()) {
            throw new IllegalStateException("Cannot Arrive if not yet rolling");
        }

        int outingId = clueRideSessionDto.getOutingId();
        LOGGER.info("Changing Game State for outing " + outingId + " to Arrival");

        gameStateBuilder.withRolling(false);
        if (isEndOfCourse(gameStateBuilder)) {
            gameStateBuilder.withOutingComplete(true);
            recordTeamBadgeEvent("courseCompleted");
        }

        LOGGER.trace("Before Synch(gameStateMap)");
        synchronized (gameStateMap) {
            gameStateMap.put(outingId, gameStateBuilder);
            ssEventService.sendArrivalEvent(outingId, gameStateBuilder.build());
        }
        LOGGER.trace("After Synch(gameStateMap)");
        recordTeamBadgeEvent("arrival");
        LOGGER.trace("After recordTeamBadgeEvent");
        return gameStateBuilder.build();
    }

    /**
     * Sends out Badge Events with the given type to everyone on the team (who has
     * checked in).
     * @param badgeEventName specific Team Event.
     */
    private void recordTeamBadgeEvent(String badgeEventName) {
        OutingView outingView = outingService.getViewById(clueRideSessionDto.getOutingId());
        OutingPlusGameState outingPlusGameState = new OutingPlusGameState(
                gameStateMap.get(outingView.getId()).build(),
                outingView
        );
        BadgeEventEntity badgeEventEntity = BadgeEventEntity.builder()
                .withTimestamp(new Date())
                .withMethodName(badgeEventName)
                .withMethodClass(this.getClass())
                .withReturnValue(outingPlusGameState);
        badgeEventService.sendToTeam(badgeEventEntity, outingView.getTeamId());
    }

    /**
     * This happens when we've received an arrival event and we're on the last path
     * of the course.
     *
     * Package Private for testing purposes.
     *
     * @param gameStateBuilder the current state which we're evaluating.
     * @return true if we're at the last location in the Course.
     */
    boolean isEndOfCourse(GameState.Builder gameStateBuilder) {
        requireNonNull(clueRideSessionDto, "expected Session to be established");
        requireNonNull(courseService, "expected courseService to be provided");

        OutingView outingView = outingService.getViewById(clueRideSessionDto.getOutingId());
        requireNonNull(outingView, "expected session to hold valid outing");
        Course course = requireNonNull(
                courseService.getById(outingView.getCourseId()),
                "Expected Outing to hold valid Course"
        );

        /* Last Location ID is the size of the Location IDs - 1. */
        int lastLocationId = course.getLocationIds().size() - 1;
        return (
                gameStateBuilder.getLocationId().equals(
                         course.getLocationIds().get(lastLocationId)
                )
        );
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
        GameState.Builder gameStateBuilder = getBuilderForSession();
        int outingId = clueRideSessionDto.getOutingId();
        LOGGER.info("Changing Game State for outing " + outingId + " to Departure");
        OutingView outingView = outingService.getViewById(outingId);

        Course course = requireNonNull(
                courseService.getById(outingView.getCourseId()),
                "Expected Outing to hold valid Course"
        );
        if (gameStateBuilder.getRolling()) {
            throw new IllegalStateException("Cannot depart if still rolling");
        }
        gameStateBuilder.withRolling(true)
                .incrementPathIndex(course.getPathIds().size());
        int locationId = updateLocation(gameStateBuilder, course);
        gameStateBuilder.withPuzzleId(
                puzzleStateService.getPuzzleStateByLocationAndOuting(
                        outingId,
                        locationId
                ).getPuzzleId()
        );
        GameState gameState = gameStateBuilder.build();

        synchronized (gameStateMap) {
            gameStateMap.put(outingId, gameStateBuilder);
            ssEventService.sendDepartureEvent(outingId, gameState);
        }
        return gameState;
    }

    /**
     * Handles two fields within the gameStateBuilder: Location ID and Next Location's Name.
     *
     * @param gameStateBuilder the mutable GameState for this session.
     * @param course the immutable course for this session.
     * @return locationId the upcoming Location's ID.
     */
    private int updateLocation(GameState.Builder gameStateBuilder, Course course) {
        int pathIndex = gameStateBuilder.getPathIndex();
        int locationIndex = pathIndex + 1;
        int nextLocationId = course.getLocationIds().get(locationIndex);
        Location nextLocation = locationService.getById(nextLocationId);
        gameStateBuilder
                .withLocationId(nextLocationId)
                .withNextLocationName(nextLocation.getName());
        return nextLocationId;
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
                    clueRideSessionDto.getOutingId(),
                    answerSummary
            );
        }
        return answerSummary;
    }

    @Override
    public GameState resetGameState(Integer outingId) {
        GameState.Builder gameStateBuilder = GameState.Builder.builder();
        gameStateMap.put(outingId, gameStateBuilder);
        return gameStateBuilder.build();
    }

    @Override
    public GameState resetDefaultOutingGameState() {
        return resetGameState(OutingConstants.ETERNAL_OUTING_ID);
    }

    @Override
    public GameState updateDefaultCourse(Integer courseId) {
        courseService.makeDefault(courseId);
        return resetGameState(OutingConstants.ETERNAL_OUTING_ID);
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
