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

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.slf4j.Logger;

import com.clueride.auth.session.ClueRideSession;
import com.clueride.auth.session.ClueRideSessionDto;
import com.clueride.domain.account.principal.BadgeOsPrincipal;
import com.clueride.domain.achievement.Achievement;
import com.clueride.domain.achievement.AchievementService;
import com.clueride.domain.step.parent.StepParentEntity;
import com.clueride.domain.step.parent.StepParentStore;

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
    private StepParentStore stepParentStore;


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

        Map<Integer, List<Achievement>> childMap;
        childMap = getChildAchievementsPerParentBadge(userId);

        for (int badgeId : childMap.keySet()) {
            BadgeProgressEntity badgeProgress = badgeProgressStore.getBadgeProgressById(badgeId);
            badgeProgress.withAchievements(childMap.get(badgeId));
            badgeProgressList.add(badgeProgress.build());
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

        /* Get the (static) child to parent relationship across all badges/steps. */
        Map<Integer, Integer> parentMap = getParentMap();

        /* Map out the list of children per parent. */
        for (Achievement achievement : achievements) {
            int achievementId = achievement.getStepId();
            Integer parentId = parentMap.get(achievementId);
            if (parentId != null) {
                LOGGER.debug("Achievement {} is earned by Step {}", parentMap.get(achievementId), achievementId);
                List<Achievement> children = childMap.computeIfAbsent(
                        parentId,
                        k -> new ArrayList<>()
                );
                children.add(achievement);
            }
        }
        LOGGER.info("List of Achievements in Progress: {}", childMap.keySet());
        return childMap;
    }

    /**
     * Retrieves the mapping between Steps and their Parents.
     *
     * @return Map with StepID as the key and ParentID as the value.
     */
    private Map<Integer, Integer> getParentMap() {
        Map<Integer, Integer> parentMap = new HashMap<>();
        List<StepParentEntity> stepParents = stepParentStore.getAllStepParents();
        for (StepParentEntity stepParent : stepParents) {
            parentMap.put(stepParent.getId(), stepParent.getParentId());
        }
        return parentMap;
    }

}
