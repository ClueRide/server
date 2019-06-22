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
 * Created by jett on 6/17/19.
 */
package com.clueride.domain.achievement.raw;

/**
 * Defines operations on the Raw Achievement information
 * available under the `meta_key` named `_badgeos_achievements`.
 */
public interface RawAchievementStore {

    /**
     * Retrieves the record available for a given user.
     * @param userId Unique identifier for the User.
     * @return Instance of {@link RawAchievement} for the given user.
     */
    RawAchievement getRawAchievementsForUser(int userId);

}
