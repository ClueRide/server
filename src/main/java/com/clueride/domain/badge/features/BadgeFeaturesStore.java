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
 * Created by jett on 6/15/19.
 */
package com.clueride.domain.badge.features;

import java.util.List;

/**
 * Defines persistence operations on {@link BadgeFeatures} instances.
 *
 * These are read-only instances. Creating records is performed within
 * the BadgeOS system.
 */
public interface BadgeFeaturesStore {

    /**
     * Retrieves the full list of Badges that can be earned.
     *
     * @return Complete List of BadgeFeatures.
     */
    List<BadgeFeaturesEntity> getAllBadgeFeatures();

    /**
     * Retrieves list of Themed Badges; both open and closed.
     * @return List of Themed {@link BadgeFeaturesEntity}.
     */
    List<BadgeFeaturesEntity> getThemedBadgeFeatures();

}
