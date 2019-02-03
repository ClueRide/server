/*
 * Copyright 2018 Jett Marks
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
 * Created by jett on 9/15/18.
 */
package com.clueride.domain.game.ssevent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;

import com.clueride.auth.ClueRideSession;
import com.clueride.auth.ClueRideSessionDto;
import com.clueride.config.ConfigService;
import com.clueride.domain.game.GameState;


/**
 * Sends formatted JSON string representing expected SSE event message to Broadcast server for SSE.
 *
 * Picks up the SSE Host information from System Property.
 */
public class SSEventServiceImpl implements SSEventService {
    private final Logger LOGGER;
    private final String sseHost;

    @Inject
    @SessionScoped
    @ClueRideSession
    private ClueRideSessionDto clueRideSessionDto;

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Injectable constructor; allows logging the configuration upon construction.
     * @param configService - Service providing which host is used for SSE.
     * @param logger - How we log.
     */
    @Inject
    public SSEventServiceImpl(
            ConfigService configService,
            Logger logger
    ) {
        this.sseHost = configService.get("sse.host");
        this.LOGGER = logger;
        LOGGER.debug("Communicating with SSE server at " + sseHost);
    }

    @Override
    public Integer sendTeamReadyEvent(Integer outingId, GameState gameState) {
        return sendEvent(eventMessageFromString("Team Assembled", gameState));
    }

    @Override
    public Integer sendArrivalEvent(Integer outingId, GameState gameState) {
        return sendEvent(eventMessageFromString("Arrival", gameState));
    }

    @Override
    public Integer sendDepartureEvent(Integer outingId, GameState gameState) {
        return sendEvent(eventMessageFromString("Departure", gameState));
    }

    /** Builds the Event from Session information including the Game State. */
    private String eventMessageFromString(String event, GameState gameState) {
        SSEventMessage ssEventMessage =
                new SSEventMessage(
                        event,
                        clueRideSessionDto.getOutingView().getId(),
                        gameState
                );
        try {
            return objectMapper.writeValueAsString(
                    ssEventMessage
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";

//        return String.format("{\"event\":\"%s\",\"outingId\":%d}", event, outingId);
    }

    /**
     * Bundles up the formatted message for sending as a post to the game state broadcast URL.
     * @param message JSON-formatted plain text to be sent as the message.
     * @return the HTTP Response code (exception thrown if it's not 200).
     */
    private Integer sendEvent(
            String message
    ) {
        int responseCode = 500;
        try {
            URL url = urlForSession();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");

            OutputStream os = conn.getOutputStream();
            os.write(message.getBytes());
            os.flush();

            responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + responseCode);
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            conn.getInputStream()
                    )
            );

            String output;
            LOGGER.debug("Output from SSE Server:");
            while ((output = br.readLine()) != null) {
                LOGGER.debug(output);
            }

            conn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseCode;
    }

    /** Builds appropriate URL based on the Session's Outing ID. */
    private URL urlForSession() throws MalformedURLException {
        return new URL(
                "http://" + sseHost
                        + "/rest/game-state-broadcast/"
                        + clueRideSessionDto.getOutingView().getId()
        );
    }

    class SSEventMessage {
        String event;
        Integer outingId;
        GameState gameState;

        SSEventMessage(String event, Integer id, GameState gameState) {
            this.event = event;
            this.outingId = id;
            this.gameState = gameState;
        }

        public String getEvent() {
            return event;
        }

        public Integer getOutingId() {
            return outingId;
        }

        public GameState getGameState() {
            return gameState;
        }

    }

}
