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
 * Created by jett on 1/21/19.
 */
package com.clueride.domain.game;

import com.clueride.auth.Secured;
import com.clueride.domain.course.CourseEntity;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * REST API for Game State.
 */
@Path("/game-state")
public class GameStateWebService {

    @Inject
    private GameStateService gameStateService;

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public GameState getActiveSessionGameState() {
        return gameStateService.getActiveSessionGameState();
    }

    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("team-assembled")
    public GameState updateOutingWithTeamAssembled() {
        return gameStateService.updateWithTeamAssembled();
    }

    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("arrival")
    public GameState updateOutingWithArrival() {
        return gameStateService.updateOutingStateWithArrival();
    }

    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("departure")
    public GameState updateOutingWithDeparture() {
        return gameStateService.updateOutingStateWithDeparture();
    }

    @DELETE
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public GameState resetActiveSessionGameState() {
        return gameStateService.resetDefaultOutingGameState();
    }

    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("default-course")
    public GameState updateDefaultCourse(CourseEntity courseEntity) {
        return gameStateService.updateDefaultCourse(courseEntity.getId());
    }

}
