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

/**
 * Defines persistence operations on {@link Step} instances.
 *
 * These are read-only; the BadgeOS system is used to create
 * new Step records.
 */
public interface StepStore {

    /**
     * Retrieve all instances of Steps.
     * @return Complete List of all Steps.
     */
    List<StepEntity> getAllSteps();

    /**
     * Retrieve instances of Step for the given Badge.
     * @param badgeId unique identifier for a given Badge.
     * @return List of Steps to complete before Badge can be earned.
     */
    Iterable<? extends StepEntity> getStepsForBadge(int badgeId);

}
