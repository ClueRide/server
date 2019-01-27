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
 * Created by jett on 1/26/19.
 */
package com.clueride.domain.location;

import java.util.List;

import com.clueride.domain.course.Course;

/**
 * Defines operations on {@link Location} instances.
 */
public interface LocationService {

    /**
     * For an open session (which will have an Outing & Course),
     * return the list of ordered Locations that support the {@link Course}.
     * @return List of Location for the current session's Course; empty list
     * if no session or Course is available.
     */
    List<Location> getSessionLocationsWithGeoJson();

}
