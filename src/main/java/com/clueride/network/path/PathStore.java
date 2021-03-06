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

import com.clueride.domain.path.Path;

/**
 * Defines persistence operations on a {@link Path} instance.
 */
public interface PathStore {
    /**
     * Returns the GeoJSON -- as assembled by the database PostGIS functions --
     * represented by the Path ID provided.
     *
     * The GeoJSON is able to represent properties for the Geometry. These
     * properties will vary depending on whether you're playing the game,
     * assembling a Course, or maintaining the overall network.
     *
     * @param pathId unique identifier for the Path.
     * @return String representation of GeoJson, a Feature Collection
     * with an ordered set of LineString.
     */
    String getPathGeoJson(Integer pathId);

    /**
     * Returns number of Edge records for the given Path ID.
     *
     * @param pathId unique identifier for the Path.
     * @return count of the number of Edge records for the Path.
     */
    Integer getEdgeCount(Integer pathId);

    /**
     * TBD: what needs to be provided so we can persist an edge?
     *
     * @param pathId unique identifier for the Path.
     * @return Unique ID of the new Edge.
     */
    Integer addEdge(Integer pathId);

}
