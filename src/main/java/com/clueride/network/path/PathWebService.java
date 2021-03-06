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
 * Created by jett on 1/20/19.
 */
package com.clueride.network.path;

import com.clueride.auth.Secured;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * REST API for the GeoJson representation of Path instances.
 *
 * The entire sequence of edges is collapsed down to a single line.
 */
@Path("/path")
public class PathWebService {

    @Inject
    private PathService pathService;

    @GET
    @Secured
    @Path("geojson/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPathGeoJsonById(@PathParam("id") Integer pathId) {
        return pathService.getPathGeoJsonById(pathId);
    }

}
