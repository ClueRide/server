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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.clueride.domain.path.PathForCourseBuilder;
import com.clueride.domain.path.PathForCourseStore;
import static java.util.Objects.requireNonNull;

/**
 * Default implementation of {@link PathService}.
 */
public class PathServiceImpl implements PathService {
    @Inject
    private Logger LOGGER;

    @Inject
    private PathStore pathStore;

    @Inject
    private PathForCourseStore pathForCourseStore;

    @Override
    public String getPathGeoJsonById(Integer pathId) {
        requireNonNull(pathId, "Path ID must be provided");
        return pathStore.getPathGeoJson(pathId);
    }

    @Override
    public List<Integer> getLocationIds(Integer courseId) {
        requireNonNull(courseId, "Course ID must be provided");

        List<PathForCourseBuilder> paths = pathForCourseStore.getPathsForCourse(courseId);
        if (paths.size() == 0) {
            LOGGER.warn("No paths found for course ID {}", courseId );
            return Collections.emptyList();
        }

        List<Integer> locationIds = new ArrayList<>();
        PathForCourseBuilder lastBuilder = null;
        for (PathForCourseBuilder builder : paths) {
            locationIds.add(builder.getStartLocationId());
            lastBuilder = builder;
        }
        locationIds.add(lastBuilder.getEndLocationId());

        return locationIds;
    }

}
