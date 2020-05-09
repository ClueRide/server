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
 * Created by jett on 1/1/19.
 */
package com.clueride.domain.course;

import java.util.List;

/**
 * Defines operations on a Course.
 */
public interface CourseService {
    /**
     * Retrieval of a Course instance matching the given ID.
     *
     * @param courseId unique identifier for the course.
     * @return matching Course.
     */
    Course getById(Integer courseId);

    /**
     * For an established session, return the Course associated with the Session.
     * @return Session's instance of {@link Course}.
     */
    Course getSessionCourse();

    /**
     * Given the Course ID, retrieve the ordered list of Location IDs for that Course.
     * @param courseId unique identifier for the Course.
     * @return Ordered list of Location IDs.
     */
    List<Integer> getAttractionIdsForCourse(Integer courseId);

    /**
     * Retrieves list of all courses sorted alphabetically by name.
     *
     * @return Ordered list of Courses.
     */
    List<Course> getAllCourses();

}
