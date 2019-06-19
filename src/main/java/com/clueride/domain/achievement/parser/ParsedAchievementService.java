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
package com.clueride.domain.achievement.parser;

import java.util.List;

/**
 * Creates ParsedAchievement instances from the RawAchievement records
 * maintained by the BadgeOS system.
 */
public interface ParsedAchievementService {
   /**
    * Given a User ID, retrieve the achievements for that user.
    * @param userId Unique ID for the user (WordPress ID).
    * @return List of ParsedAchievements for the User.
    */
   List<ParsedAchievement> getAchievementForUser(int userId);

}
