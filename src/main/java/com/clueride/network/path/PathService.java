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
 * Defines operations on {@link Path} instances.
 */
public interface PathService {

    /**
     * Retrieves the GeoJSON representation of the {@link Path} identified
     * by the pathId.
     *
     * A Path is an ordered sequence of Edge instances.
     * @param pathId unique identifier for the Path.
     * @return String formatted as a GeoJSON feature collection containing
     * the Geometry for an ordered array of LineString.
     */
    String getPathGeoJsonById(Integer pathId);

}
