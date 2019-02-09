/*
 * Copyright 2017 Jett Marks
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
 * Created by jett on 10/8/17.
 */
package com.clueride.domain.place;

import com.clueride.domain.location.LocationBuilder;
import com.clueride.domain.location.ReadinessLevel;

/**
 * Defines the operations for scoring a Location.
 */
public interface ScoredLocationService {
    /**
     * Given a Location, compute the Readiness level for that Location.
     * @param location to be evaluated.
     * @return Readiness Level
     */
    ReadinessLevel calculateReadinessLevel(LocationBuilder location);

}
