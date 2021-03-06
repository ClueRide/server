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
 * Created by jett on 1/1/19.
 */
package com.clueride.domain.outing;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.clueride.auth.Secured;

/**
 * REST API for Outings.
 */
@Path("/outing")
public class OutingWebService {
    @Inject
    private OutingService outingService;

    @GET
    @Secured
    @Path("active")
    @Produces(MediaType.APPLICATION_JSON)
    public OutingView getActiveOuting() {
        return outingService.getActiveOutingView();
    }

    @GET
    @Secured
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Outing getOutingById(@PathParam("id") Integer outingId) {
        return outingService.getById(outingId);
    }

    @GET
    @Secured
    @Path("view/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public OutingView getOutingViewById(@PathParam("id") Integer outingId) {
        return outingService.getViewById(outingId);
    }


}
