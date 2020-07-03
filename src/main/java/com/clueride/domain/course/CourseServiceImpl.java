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

import com.clueride.auth.session.ClueRideSession;
import com.clueride.auth.session.ClueRideSessionDto;
import com.clueride.network.path.PathService;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of CourseService.
 */
public class CourseServiceImpl implements CourseService {
    private final PathService pathService;
    private final CourseStore courseStore;

    @Inject
    @SessionScoped
    @ClueRideSession
    private ClueRideSessionDto clueRideSessionDto;

    @Inject
    public CourseServiceImpl(
            PathService pathService,
            CourseStore courseStore
    ) {
        this.pathService = pathService;
        this.courseStore = courseStore;
    }

    @Override
    public Course getSessionCourse() {
        return getById(
                clueRideSessionDto.getOutingView().getCourseId()
        );
    }

    @Override
    public List<Integer> getAttractionIdsForCourse(Integer courseId) {
        return pathService.getLocationIds(courseId);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseStore.getCourses().stream().map(c -> c.build()).collect(Collectors.toList());
    }

    @Override
    public Course addNewCourse(CourseEntity courseEntity) {
        try {
            courseStore.addNew(courseEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return courseEntity.build();
    }

    @Override
    public Course getById(final Integer courseId) {
        CourseEntity courseEntity = courseStore.getCourseById(courseId);
        if (courseEntity == null) {
            throw new CourseNotFoundException("Course ID: " + courseId);
        }

        return courseEntity
                .withLocationIds(pathService.getLocationIds(courseId))
                .build();
    }

}
