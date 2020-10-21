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
 * Created by jett on 1/13/19.
 */
package com.clueride.network.edge;

import com.clueride.auth.Secured;
import com.clueride.network.edge.upload.EdgeUploadRequest;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * REST API for {@link Edge} instances.
 */
@Path("/edge")
public class EdgeWebService {
    @Inject
    private EdgeService edgeService;

    @GET
    @Secured
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Edge getEdgeById(@PathParam("id") Integer edgeId) {
        return edgeService.getEdgeById(edgeId);
    }

    @GET
    @Secured
    @Path("geojson/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getEdgeGeoJsonById(@PathParam("id") Integer edgeId) {
        return edgeService.getEdgeGeoJsonById(edgeId);
    }

    @POST
    @Secured
    @Path("{pathId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Edge addEdgeToPath(
            @PathParam("pathId") Integer pathId,
            @MultipartForm EdgeUploadRequest edgeUploadRequest
    ) {
        return edgeService.addEdgeToPath(pathId, edgeUploadRequest);
    }
}
