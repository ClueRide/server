/*
 * Copyright 2016 Jett Marks
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
 * Created by jett on 1/15/16.
 */
package com.clueride.domain.course;

import java.net.URL;
import java.util.List;

import com.clueride.domain.location.Location;

/**
 * Representation of a Course, including an ordered list of Locations and the Paths
 * which move from one Location to the next.
 */
public interface Course {
    Integer getId();
    String getName();
    String getDescription();
    URL getUrl();
    Integer getCourseTypeId();
    List<Integer> getLocationIdList();
    List<Integer> getPathIds();
    Location getDeparture();
    Location getDestination();

    // TODO: CA-374
//    Step nextStep();
//    Step currentStep();

}
