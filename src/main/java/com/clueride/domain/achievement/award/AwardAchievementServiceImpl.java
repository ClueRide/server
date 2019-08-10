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

import com.clueride.aop.badge.MethodName;
import com.clueride.badgeos.BadgeOSSessionService;
import com.clueride.domain.achievement.map.ArrivalStepsMapService;
import com.clueride.domain.achievement.map.PageStepsMapService;
import com.clueride.domain.badge.event.BadgeEvent;
import com.clueride.domain.game.OutingPlusGameState;
import com.clueride.domain.page.Page;
import com.clueride.domain.puzzle.answer.AnswerSummary;

/**
 * Implementation of {@link AwardAchievementService}.
 */
public class AwardAchievementServiceImpl implements AwardAchievementService {
    @Inject
    private Logger LOGGER;

    private final ArrivalStepsMapService arrivalStepsMapService;
    private final BadgeOSSessionService badgeOSSessionService;
    private final PageStepsMapService pageStepsMapService;

    private static Map<Integer, List<Integer>> arrivalStepsMap;
    private static Map<Integer, List<Integer>> pageStepsMap;

    @Inject
    public AwardAchievementServiceImpl(
            ArrivalStepsMapService arrivalStepsMapService,
            BadgeOSSessionService badgeOSSessionService,
            PageStepsMapService pageStepsMapService
    ) {
        this.arrivalStepsMapService = arrivalStepsMapService;
        this.badgeOSSessionService = badgeOSSessionService;
        this.pageStepsMapService = pageStepsMapService;
        arrivalStepsMap = populateArrivalStepsMap();
        pageStepsMap = pageStepsMapService.loadMap();
    }

    @Override
    public void awardPotentialAchievement(BadgeEvent badgeEvent) {
        // TODO: SVR-61 Send the event to BadgeOS if appropriate
        if (badgeEvent.getReturnValue() instanceof OutingPlusGameState) {
            awardGameStateAchievement(badgeEvent);
            return;
        } else if (badgeEvent.getReturnValue() instanceof AnswerSummary) {
            LOGGER.info("Checking if we can award Answering a question");
            return;
        }

        MethodName methodName = MethodName.fromMethodName(badgeEvent.getMethodName());
        switch (methodName) {
            case REGISTER:
                LOGGER.info("Awarding Registration Achievement");
                this.awardAchievement(
                        badgeEvent.getBadgeOSId(),
                        3615
                );
                return;
            case VISIT_PAGE:
                LOGGER.info("Checking if we can award Visit Page Achievement");
                Page page = (Page) badgeEvent.getReturnValue();
                if (page != null) {
                    if (page.getPageSlug() != null) {
                        awardPageVisit(
                                badgeEvent.getBadgeOSId(),
                                page
                        );
                    } else {
                        LOGGER.error("Not yet handling pages outside of Word Press");
                    }
                }
                return;
            default:
                break;
        }

        LOGGER.warn("Not yet checking if we can award this event");

    }

    @Override
    public void awardPageVisit(
            Integer badgeOSId,
            Page page
    ) {
        if (!pageStepsMap.containsKey(page.getPostId())) {
            LOGGER.error("Missing the page {} in the map", page.getPageSlug());
            return;
        }

        List<Integer> pageStepIds = pageStepsMap.get(page.getPostId());

        for (Integer pageStepId : pageStepIds) {
            awardAchievement(
                    badgeOSId,
                    pageStepId
            );
        }

    }

    @Override
    public void awardPageVisit(
            Integer badgeOSId,
            Page page,
            Integer attractionId
    ) {
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

    private void awardGameStateAchievement(BadgeEvent badgeEvent) {
        OutingPlusGameState outingPlusGameState = (OutingPlusGameState) badgeEvent.getReturnValue();
        MethodName methodName = MethodName.fromMethodName(badgeEvent.getMethodName());
        switch (methodName) {
            case ARRIVAL:
                LOGGER.debug("Awarding Arrival");
                this.awardArrival(
                        badgeEvent.getBadgeOSId(),
                        outingPlusGameState.getGameState().getNextLocationName(),
                        outingPlusGameState.getGameState().getLocationId()
                );
                break;
            case TEAM_ASSEMBLED:
                LOGGER.debug("Awarding Team Assembled");
                break;
            default:
                LOGGER.error("Unrecognized methodName: {}", badgeEvent.getMethodName());
                break;
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
