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
import com.clueride.domain.outing.NoSessionOutingException;
import com.clueride.domain.outing.OutingService;
import com.clueride.network.path.PathService;
import org.slf4j.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

/**
 * Implementation of CourseService.
 */
public class CourseServiceImpl implements CourseService {
    @Inject
    private Logger LOGGER;

    private final PathService pathService;
    private final CourseStore courseStore;
    private final OutingService outingService;

    @Inject
    @SessionScoped
    @ClueRideSession
    private ClueRideSessionDto clueRideSessionDto;

    @Inject
    public CourseServiceImpl(
            PathService pathService,
            CourseStore courseStore,
            OutingService outingService
            ) {
        this.pathService = pathService;
        this.courseStore = courseStore;
        this.outingService = outingService;
    }

    @Override
    public Course getSessionCourse() {
        requireNonNull(clueRideSessionDto, "Session not established");

        if (clueRideSessionDto.hasNoOuting()) {
            throw new NoSessionOutingException();
        }
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
    public Course updateCourse(CourseEntity courseEntity) {
        LOGGER.debug("Updating {}", courseEntity.getName());
        prepareCourseAttractionsForUpdate(courseEntity);
        courseStore.update(courseEntity);
        return courseEntity.build();
    }

    @Override
    public Course makeDefault(CourseEntity courseEntity) {
        LOGGER.debug("Setting Course {} to be the Default", courseEntity.getName());
        outingService.setCourseForEternalOuting(courseEntity.getId());
        return courseEntity.build();
    }

    private void prepareCourseAttractionsForUpdate(CourseEntity courseEntity) {
        List<Integer> attractionIds = courseEntity.getLocationIds();
        if (isNull(attractionIds) || attractionIds.size() == 0) {
            LOGGER.debug("No Attraction IDs in this Course");
            return;
        }

        courseEntity.withStartingAttractionId(attractionIds.get(0));
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
