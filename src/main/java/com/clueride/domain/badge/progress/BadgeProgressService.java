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
package com.clueride.domain.badge.progress;

import java.util.List;

/**
 * Defines operations on {@link BadgeProgress} instances.
 */
public interface BadgeProgressService {

    /**
     * For the current session, return an ordered list of BadgeProgress
     * instances.
     *
     * @return List of {@link BadgeProgress} instances.
     */
    List<BadgeProgress> getBadgeProgressForSession();

    /**
     * For the given User ID, return an ordered list of BadgeProgress
     * instances.
     *
     * @param userId Unique identifier for the User.
     * @return List of {@link BadgeProgress} instances.
     */
    List<BadgeProgress> getBadgeProgressForUser(Integer userId);

}
