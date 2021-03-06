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
 * Created by jett on 1/27/19.
 */
package com.clueride.domain.puzzle;

import com.clueride.aop.badge.BadgeCapture;
import com.clueride.auth.Secured;
import com.clueride.domain.game.GameStateService;
import com.clueride.domain.location.LocationEntity;
import com.clueride.domain.puzzle.answer.AnswerEntity;
import com.clueride.domain.puzzle.answer.AnswerPost;
import com.clueride.domain.puzzle.answer.AnswerSummary;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * REST API for {@link Puzzle} instances and their {@link AnswerEntity} instances.
 */
@Path("puzzle")
public class PuzzleWebService {

    @Inject
    private PuzzleService puzzleService;

    @Inject
    private GameStateService gameStateService;

    @GET
    @Secured
    @Path("location/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Puzzle> getPuzzlesByLocationId(@PathParam("id") Integer locationId) {
        return puzzleService.getByLocation(locationId);
    }

    @POST
    @Secured
    @Path("blank")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Puzzle getBlankPuzzle(LocationEntity locationEntity) {
        return puzzleService.getBlankPuzzleForLocation(locationEntity);
    }

    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Puzzle savePuzzle(PuzzleEntity puzzleEntity) {
        return puzzleService.addOrUpdate(puzzleEntity);
    }

    @POST
    @Secured
    @BadgeCapture
    @Path("answer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AnswerSummary postAnswerForSession(AnswerPost answerPost) {
        return gameStateService.postAnswer(answerPost);
    }

}
