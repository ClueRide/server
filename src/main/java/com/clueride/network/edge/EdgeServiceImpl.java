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

import com.clueride.domain.path.edge.PathEdgeEntity;
import com.clueride.domain.path.edge.PathEdgeStore;
import com.clueride.imp.gpx.GpxToEdge;
import com.clueride.network.edge.upload.EdgeUploadRequest;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Default implementation for {@link EdgeService}.
 */
public class EdgeServiceImpl implements EdgeService {
    @Inject
    private Logger LOGGER;

    @Inject
    private EdgeStore edgeStore;

    @Inject
    private GpxToEdge gpxToEdge;

    @Inject
    private PathEdgeStore pathEdgeStore;

    @Override
    public List<Edge> getPathEdges(Integer pathId) {
        return null;
    }

    @Override
    public Edge getEdgeById(Integer edgeId) {
        requireNonNull(edgeId, "Edge ID must be provided");
        EdgeEntity edgeEntity = edgeStore.getEdgeById(edgeId);
        LOGGER.debug(edgeEntity.toString());
        return edgeEntity.build();
    }

    @Override
    public String getEdgeGeoJsonById(Integer edgeId) {
        requireNonNull(edgeId, "Edge ID must be provided");
        return edgeStore.getEdgeGeoJson(edgeId);
    }

    @Override
    public Edge addEdgeToPath(Integer pathId, EdgeUploadRequest edgeUploadRequest) {
        LOGGER.debug("Adding Edge to the Path with ID " + pathId);

        /* First, create the Edge. */
        EdgeEntity edgeEntity = gpxToEdge.edgeFromGpxStream(edgeUploadRequest.getFileData());
        edgeStore.add(edgeEntity);

        /* Now that we have the Edge we want to put in the Path, let's update the Path. */
        PathEdgeEntity pathEdgeEntity = PathEdgeEntity.builder()
                .withPathId(pathId)
                .withEdgeId(edgeEntity.getId())
                .withEdgeOrder(1);
        pathEdgeStore.createNew(pathEdgeEntity);

        /* Return newly created Edge. */
        return edgeEntity.build();
    }
}
