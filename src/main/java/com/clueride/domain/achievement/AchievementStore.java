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
 * Created by jett on 8/20/19.
 */
package com.clueride.domain.achievement;

import java.util.List;

/**
 * Defines persistence operations on Achievements.
 */
public interface AchievementStore {

    /**
     * Returns full list of all achievements.
     * @return all achievements.
     */
    List<AchievementEntity> getAllAchievements();

    /**
     * Returns list of achievements for the given User.
     * @param userId unique identifier for the user.
     * @return List of {@link Achievement} for the given user.
     */
    List<AchievementEntity> getAchievementsForUser(Integer userId);

    /**
     * Clears the given record, erasing the achievement.
     * Primarily used for testing, but can also be used to correct mistakes.
     * @param achievementId Unique identifier within the achievement table.
     */
    void removeAchievement(Integer achievementId);

}
