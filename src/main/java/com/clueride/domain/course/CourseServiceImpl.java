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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import com.clueride.domain.location.Location;
import com.clueride.network.path.PathService;

/**
 * Interim implementation.
 */
public class CourseServiceImpl implements CourseService {
    private final PathService pathService;

    @Inject
    public CourseServiceImpl(
            PathService pathService
    ) {
        this.pathService = pathService;
    }

    @Override
    public Course getSessionCourse() {
        return getById(1);
    }

    @Override
    public List<Integer> getLocationIdsForCourse(Integer courseId) {
        return pathService.getLocationIds(courseId);
    }

    @Override
    public Course getById(final Integer courseId) {
        /* Interim implementation. */
        // TODO: sort out whether the Path Service should be providing this.
        final List<Integer> interimPathIds = Arrays.asList(
                6,
                4,
                3,
                13,
                9,
                10,
                2
        );

        final List<Integer> interimLocations = pathService.getLocationIds(courseId);

        return new Course(){

            @Override
            public Integer getId() {
                return courseId;
            }

            @Override
            public String getName() {
                return "Five Free Things";
            }

            @Override
            public String getDescription() {
                return "From an article that I can no longer find";
            }

            @Override
            public Integer getCourseTypeId() {
                return null;
            }

            @Override
            public List<Integer> getLocationIdList() {
                return interimLocations;
            }

            @Override
            public List<Integer> getPathIds() {
                return interimPathIds;
            }

            @Override
            public Location getDeparture() {
                return null;
            }

            @Override
            public Location getDestination() {
                return null;
            }

            @Override
            public URL getUrl() {
                URL url = null;
                try {
                    url = new URL("https://clueride.com/five-free-things/");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return url;
            }

        };

    }

}
