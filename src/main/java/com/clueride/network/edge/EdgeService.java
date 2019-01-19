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

import java.util.List;

import com.clueride.domain.path.Path;

/**
 * Defines operations on {@link Edge} instances.
 */
public interface EdgeService {

    /**
     * Retrieves a list of the Edges making up a Path.
     * @param pathId unique identifier for a {@link Path}.
     * @return List of Edges.
     */
    List<Edge> getPathEdges(Integer pathId);

    Edge getEdgeById(Integer edgeId);

    /**
     * Retrieves the GeoJSON representation of the Edge identified by the given ID.
     * @param edgeId unique identifier for the Edge.
     * @return String formatted as a GeoJSON feature collection containing the Geometry for a LineString.
     */
    String getEdgeGeoJsonById(Integer edgeId);

}
