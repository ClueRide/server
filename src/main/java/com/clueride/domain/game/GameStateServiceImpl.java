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
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clueride.auth.ClueRideSession;
import com.clueride.auth.ClueRideSessionDto;
import com.clueride.domain.game.ssevent.SSEventService;

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

    /** Cached copy of GameState TODO: Persist this via a service. */
    private final static Map<Integer, GameState> gameStateMap = new HashMap<>();

    @Inject
    public GameStateServiceImpl(
            SSEventService ssEventService
    ) {
        this.ssEventService = ssEventService;
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
        int outingId = clueRideSessionDto.getOutingView().getId();
        LOGGER.info("Opening Game State for outing " + outingId);
        GameState gameState = GameState.Builder.builder()
                .withTeamAssembled(true)
                .build();

        synchronized (gameStateMap) {
            gameStateMap.put(outingId, gameState);
            ssEventService.sendTeamReadyEvent(outingId, gameState);
        }
        return gameState;
    }

    @Override
    public GameState updateOutingStateWithArrival() {
        int outingId = clueRideSessionDto.getOutingView().getId();
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

        /* TODO: SVR-9 - Set Completed flag if this was the last location. */

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
        GameState gameState = gameStateMap.get(outingId);
        if (gameState.getRolling()) {
            throw new IllegalStateException("Cannot depart if still rolling");
        }
        GameState.Builder gameStateBuilder = GameState.Builder.from(gameState);
        gameStateBuilder.withRolling(true)
                .withPathIndex(gameStateBuilder.getPathIndex() + 1);

        gameState = gameStateBuilder.build();

        synchronized (gameStateMap) {
            gameStateMap.put(outingId, gameState);
            ssEventService.sendDepartureEvent(outingId, gameState);
        }
        return gameState;
    }

}
