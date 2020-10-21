/*
 * Copyright 2017 Jett Marks
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
 * Created by jett on 10/12/17.
 */
package com.clueride.domain.path;

import com.clueride.domain.course.Course;
import com.clueride.domain.path.meta.PathMeta;

import java.util.List;

/**
 * Defines operations on Paths.
 */
public interface PathService {
    /**
     * Given an ID, retrieve the Path instance matching the ID.
     * @param id unique identifier for the Path.
     * @return fully-populated Path instance matching the ID.
     */
    Path getById(Integer id);

    /**
     * Retrieve all Paths available to the system.
     * @return List of all Paths.
     */
    List<Path> getAll();

    /**
     * When building a Course, lists of Attractions are turned into
     * a list of Path Meta information linking those Attractions.
     *
     * The list of Attractions is a source record, so if an attraction is
     * removed from a prior list, or if the order is changed, the ordered
     * set of CourseToPath records must be changed to match this given list
     * of Attraction IDs.
     *
     * @param courseId Unique identifier to for the {@link Course}.
     * @param attractionIds ordered List of Attraction IDs to link together.
     * @return ordered List of the {@link PathMeta} instances that link together the Attractions.
     */
    List<PathMeta> getPathMetaForAttractions(Integer courseId, List<Integer> attractionIds);

}
