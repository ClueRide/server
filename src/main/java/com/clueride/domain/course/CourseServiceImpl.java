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
import java.util.Collections;
import java.util.List;

import com.clueride.domain.location.Location;

/**
 * Interim implementation.
 */
public class CourseServiceImpl implements CourseService {
    @Override
    public Course getSessionCourse() {
        return getById(1);
    }

    @Override
    public Course getById(final Integer courseId) {
        /* Interim implementation. */
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
            public List<Step> getSteps() {
                return Collections.EMPTY_LIST;
            }

            @Override
            public List<Integer> getPathIds() {
                return Arrays.asList(
                        6,
                        4,
                        3,
                        13,
                        9,
                        10,
                        2
                );
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
