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

import java.util.List;

import com.clueride.domain.course.Course;
import com.clueride.domain.location.Location;
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
     * @param pathId
     * @return String formatted as a GeoJSON feature collection containing
     * the Geometry for an ordered array of LineString.
     */
    String getPathGeoJsonById(Integer pathId);

    /**
     * Retrieves the ordered list of {@link Location} IDs for the course identified
     * by the courseId.
     * @param courseId unique identifier for the {@link Course}.
     * @return Ordered list of the Location IDs for the Course.
     */
    List<Integer> getLocationIds(Integer courseId);

}
