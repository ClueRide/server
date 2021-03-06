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
 * Defines operations on BadgeFeatures.
 */
public interface BadgeFeaturesService {

    /**
     * Retrieve full list of Badge Feature instances.
     * @return Complete List of BadgeFeatures.
     */
    List<BadgeFeatures> getAllBadgeFeatures();

    /**
     * Retrieve all Badge Features instances for Themed Badges; both open and closed.
     * @return List of {@link BadgeFeatures} that are Themed.
     */
    List<BadgeFeatures> getThemedBadgeFeatures();

}
