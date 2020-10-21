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

/**
 * Defines persistence operations on an {@link Edge} instance.
 */
public interface EdgeStore {
    EdgeEntity getEdgeById(Integer id);

    /**
     * Since the database has the functionality to assemble the GeoJson String, let's try it out.
     *
     * @param edgeId unique Identifier for the Edge.
     * @return String representation of GeoJson, a Feature with LineString Geometry and Properties.
     */
    String getEdgeGeoJson(Integer edgeId);

    /**
     * Creates a new instance from the given {@link EdgeEntity} instance.
     * @param edgeEntity newly populated EdgeEntity.
     */
    void add(EdgeEntity edgeEntity);

}
