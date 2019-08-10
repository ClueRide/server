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
 * Created by jett on 7/28/19.
 */
package com.clueride.domain.achievement.map;

import java.util.List;
import java.util.Map;

/**
 * Defines operations on a Map from Arrival events to the BadgeOS Steps
 * that should be awarded for each Arrival.
 */
public interface ArrivalStepsMapService {

    /**
     * Builds a Map from an Attraction over to the Step whose
     * achievement IDs are used to award arriving at that Attraction.
     * @return Map from Attraction Name to a List of Integer Achievement IDs.
     */
    Map<Integer, List<Integer>> buildArrivalStepsMap();

    /**
     * Retrieves Map (which may be cached).
     * @return Map from Attraction ID to a List of Integer Achievement IDs.
     */
    Map<Integer, List<Integer>> loadArrivalStepsMap();

    /**
     * Checks that each Attraction in the list has been mapped to an Achievement ID.
     * This can be run against a course to see if there are any Attractions that
     * do not have a theme assigned.
     * @param attractionIds List of Attraction IDs to be validated.
     * @return number of Attractions in the list that do not have an Achievement ID.
     */
    int validate(List<Integer> attractionIds);

}
