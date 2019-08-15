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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.slf4j.Logger;

import com.clueride.auth.session.ClueRideSession;
import com.clueride.auth.session.ClueRideSessionDto;
import com.clueride.domain.account.principal.BadgeOsPrincipal;
import com.clueride.domain.achievement.Achievement;
import com.clueride.domain.achievement.AchievementService;
import com.clueride.domain.step.StepEntity;
import com.clueride.domain.step.StepService;

/**
 * Implementation of BadgeProgressService.
 */
public class BadgeProgressServiceImpl implements BadgeProgressService {

    @Inject
    private Logger LOGGER;

    @Inject
    @SessionScoped
    @ClueRideSession
    private ClueRideSessionDto clueRideSessionDto;

    @Inject
    private BadgeProgressStore badgeProgressStore;

    @Inject
    private AchievementService achievementService;

    @Inject
    private StepService stepService;


    /**
     * Retrieves the Badges in Progress for the session's user.
     *
     * This starts with the list of achievements, and then walks through each
     * achievement to build a list of Badges these achievements contribute toward.
     * The full description of the badge is retrieved, and then the
     * achievements which contribute toward that badge are added.
     *
     * @return List of Progress Badges for the session's user.
     */
    @Override
    public List<BadgeProgress> getBadgeProgressForSession() {
        BadgeOsPrincipal badgeOsPrincipal = clueRideSessionDto.getBadgeOsPrincipal();
        Integer userId = badgeOsPrincipal.getBadgeOsUserId();
        return getBadgeProgressForUser(userId);
    }

    @Override
    public List<BadgeProgress> getBadgeProgressForUser(Integer userId) {
        LOGGER.info("Looking up Badges for User ID " + userId);
        List<BadgeProgress> badgeProgressList = new ArrayList<>();

        Map<Integer, List<Achievement>> childMap = getChildAchievementsPerParentBadge(userId);
        Map<Integer, Set<Integer>> parentMap = stepService.getParentMap();

        for (int badgeId : childMap.keySet()) {
            BadgeProgressEntity badgeProgress = badgeProgressStore.getBadgeProgressById(badgeId);

            /* If this isn't a top-level badge, we won't find a BadgeProgress instance. */
            if (badgeProgress != null) {
                List<StepEntity> stepEntities = new ArrayList<>();
                for (Integer stepId : parentMap.get(badgeId)) {
                    stepEntities.add(stepService.getStep(stepId));
                }

                badgeProgress
                        .withAchievements(childMap.get(badgeId))
                        .withStepEntities(stepEntities);

                badgeProgressList.add(badgeProgress.build());
            }
        }
        return badgeProgressList;
    }

    private Map<Integer, List<Achievement>> getChildAchievementsPerParentBadge(Integer userId) {
        Map<Integer, List<Achievement>> childMap= new HashMap<>();
        /* Get list of all Achievements for the user. */
        List<Achievement> achievements;
        try {
            achievements = achievementService.getAchievementsForUser(userId);
        } catch (NoResultException nre) {
            achievements = new ArrayList<>();
        }

        /* Get the (static) flattened children per badge relationship across all badges with steps. */
        Map<Integer, Set<Integer>> parentMap = stepService.getParentMap();

        /* For each Achievement, check to see if there are badges that are advanced by that achievement. */
        for (Achievement achievement : achievements) {
            int achievementId = achievement.getStepId();
            for (Integer badgeId : parentMap.keySet()) {
                Set<Integer> badgeSteps = parentMap.get(badgeId);
                if (badgeSteps.contains(achievementId)) {
                    LOGGER.debug("Badge {} is earned by Step {}", badgeId, achievementId);
                    List<Achievement> children = childMap.computeIfAbsent(
                            badgeId,
                            k -> new ArrayList<>()
                    );
                    children.add(achievement);
                }
            }
        }
        LOGGER.info("List of Badges in Progress: {}", childMap.keySet());
        return childMap;
    }

}
