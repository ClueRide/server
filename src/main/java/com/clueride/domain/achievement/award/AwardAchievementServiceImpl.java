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
 * Created by jett on 7/27/19.
 */
package com.clueride.domain.achievement.award;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.clueride.badgeos.BadgeOSSessionService;
import com.clueride.domain.achievement.map.ArrivalStepsMapService;

/**
 * Implementation of {@link AwardAchievementService}.
 */
public class AwardAchievementServiceImpl implements AwardAchievementService {
    @Inject
    private Logger LOGGER;

    private final ArrivalStepsMapService arrivalStepsMapService;
    private final BadgeOSSessionService badgeOSSessionService;

    private static Map<Integer, List<Integer>> arrivalStepsMap;

    @Inject
    public AwardAchievementServiceImpl(
            ArrivalStepsMapService arrivalStepsMapService,
            BadgeOSSessionService badgeOSSessionService
    ) {
        this.arrivalStepsMapService = arrivalStepsMapService;
        this.badgeOSSessionService = badgeOSSessionService;
        arrivalStepsMap = populateArrivalStepsMap();
    }

    @Override
    public void awardArrival(
            Integer badgeOSId,
            String attractionName,
            Integer locationId
    ) {
        if (StringUtils.isEmpty(attractionName)) {
            LOGGER.error("awardArrival: Missing the Attraction's Name");
            throw new RuntimeException("awardArrival: Missing the Attraction's Name");
        }

        if (!arrivalStepsMap.containsKey(locationId)) {
            LOGGER.warn("Arrival at Attraction {} shows no achievements", attractionName);
            return;
        }

        List<Integer> arrivalStepIds = arrivalStepsMap.get(locationId);

        for (Integer stepId : arrivalStepIds) {
            awardAchievement(badgeOSId, stepId);
        }

    }

    /**
     * Populate the map from an Attraction to the list of achievements awarded when
     * visiting that Attraction.
     *
     * @return Map from Attraction's Name over to Integer Step ID.
     */
    private Map<Integer, List<Integer>> populateArrivalStepsMap() {
        Map<Integer, List<Integer>> arrivalStepsMap = arrivalStepsMapService.loadArrivalStepsMap();
        arrivalStepsMapService.validate(Collections.emptyList());
        return arrivalStepsMap;
    }

    /**
     * Given the WordPress Badge OS ID and the Achievement ID, award the achievement to the user.
     *
     * @param badgeOSId WordPress User ID.
     * @param achievementId ID of the achievement to award.
     */
    private void awardAchievement(Integer badgeOSId, int achievementId) {
        badgeOSSessionService.awardAchievement(badgeOSId, achievementId);
    }

}
