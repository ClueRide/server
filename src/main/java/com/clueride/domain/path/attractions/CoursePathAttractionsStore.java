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
 * Created by jett on 1/27/19.
 */
package com.clueride.domain.path.attractions;

import com.clueride.domain.course.Course;

import java.util.List;

/**
 * Defines Persistence operations for {@link CoursePathAttractions} instances.
 */
public interface CoursePathAttractionsStore {
    /**
     * Retrieves the list of ordered Paths associated with the given Course ID.
     * @param courseId unique identifier for the {@link Course}.
     * @return Ordered list of the {@link CoursePathAttractionsEntity} instances defined
     * for the given course.
     */
    List<CoursePathAttractionsEntity> getPathAttractionsForCourse(Integer courseId);

    /**
     * Finds a Path that links between a pair of AttractionIDs.
     *
     * The direction is significant (but should it be?).
     *
     * @param startId Starting Attraction ID.
     * @param endId Ending Attraction ID.
     * @return First matching CoursePathAttractions record, whether it has been assigned to a Course or not.
     */
    CoursePathAttractionsEntity findSuitablePath(Integer startId, Integer endId);
}
