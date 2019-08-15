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
 * Created by jett on 6/16/19.
 */
package com.clueride.domain.step;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Defines operations on {@link Step} instances.
 */
public interface StepService {

    /**
     * Retrieves full list of Steps.
     * @return Complete List of Steps.
     */
    List<Step> getAllSteps();

    /**
     * Retrieves list of Steps for a given Badge.
     * @param badgeId unique identifier for a Badge.
     * @return List of the steps just for the given Badge.
     */
    Set<Step> getAllStepsForBadge(int badgeId);

    /**
     * Returns a full set of Step IDs for each Badge ID.
     * @return Map from Badge ID to Set of Step IDs flattened out.
     */
    Map<Integer, Set<Integer>> getParentMap();

    /**
     * For the given Step ID, return the matching Step Entity.
     *
     * @param stepId
     * @return StepEntity instance.
     */
    StepEntity getStep(Integer stepId);

}
