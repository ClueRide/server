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

    /**
     * Given the Course instance which hasn't yet been entered to the database,
     * create a new record with the details provided.
     *
     * @param courseEntity mutable instance to be created.
     * @return same course but with the ID populated from the database.
     */
    Course addNewCourse(CourseEntity courseEntity);

    /**
     * Given an existing course with modified details or list of Paths/Attractions,
     * update the relevant records.
     *
     * Note that this will update the presence or absence of Path records, not the Path records
     * themselves.
     *
     * @param courseEntity mutable instance with details regarding the course.
     * @return pretty much the same as what was passed in.
     */
    Course updateCourse(CourseEntity courseEntity);

    /**
     * Given an existing course, make it the default course for new Outings.
     *
     * This resets the Game State if the course is already the Default.
     *
     * @param courseId unique identifier for the Course to serve as the Default.
     * @return the same instance.
     */
    Course makeDefault(Integer courseId);

}
