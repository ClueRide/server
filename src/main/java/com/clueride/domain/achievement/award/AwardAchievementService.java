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
 * Created by jett on 7/26/19.
 */
package com.clueride.domain.achievement.award;

import com.clueride.domain.badge.event.BadgeEvent;
import com.clueride.domain.page.Page;

/**
 * Defines operations for awarding achievements.
 */
public interface AwardAchievementService {

    /**
     * Accepts BadgeEvent, checks to see if it is associated with an achievement, and if so,
     * invokes the BadgeOS system to award the achievement.
     *
     * @param badgeEvent instance of BadgeEvent captured when the session user performed some action.
     */
    void awardPotentialAchievement(BadgeEvent badgeEvent);

    /**
     * Award the Arrival Achievement for the Attraction specified in the BadgeEvent.
     *
     * @param badgeOSId WordPress identifier for the User being awarded the arrival.
     * @param attractionName Name of the Attraction arrived.
     * @param attractionId ID of the Attraction arrived.
     */
    void awardArrival(
            Integer badgeOSId,
            String attractionName,
            Integer attractionId
    );

    /**
     * Award a Page Visit achievement to the given user for the given URL.
     *
     * Each page visit -- as far as I can see -- will be to a page found
     * within the Word Press website OR be related to a given Attraction. This
     * method handles the Word Press Page Slug case without a location.
     *
     * @param badgeOSId WordPress identifier for the User being awarded the arrival.
     * @param page instance of the page visited.
     */
    void awardPageVisit(
            Integer badgeOSId,
            Page page
    );

    /**
     * Award a Page Visit achievement to the given user for the given URL.
     *
     * Each page visit -- as far as I can see -- will be to a page found
     * within the Word Press website OR be related to a given Attraction.
     *
     * @param badgeOSId WordPress identifier for the User being awarded the arrival.
     * @param page instance of the page visited.
     * @param attractionId ID of the Attraction whose page is visited.
     */
    void awardPageVisit(
            Integer badgeOSId,
            Page page,
            Integer attractionId
    );

}
