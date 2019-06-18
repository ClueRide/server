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
 * Created by jett on 6/20/19.
 */
package com.clueride.domain.badge.progress;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.clueride.domain.achievement.Achievement;
import com.clueride.domain.step.Step;

/**
 * Defines methods for computing Progress from a list
 * of Steps and a List of Achievements.
 */
public class Progress {
    private final List<Step> steps;
    private final List<Achievement> achievements;


    public Progress(
            List<Step> steps,
            List<Achievement> achievements
    ) {
        this.steps = steps;
        this.achievements = achievements;
    }

    public int getTotalSteps() {
        Set<Integer> stepIdSet = new HashSet<>();
        for (Step step : steps) {
            stepIdSet.add(step.getId());
        }
        return stepIdSet.size();
    }

    public int getTotalAchievements() {
        Set<Integer> achievementIdSet = new HashSet<>();
        for (Achievement achievement : achievements) {
            achievementIdSet.add(achievement.getStepId());
        }
        return achievementIdSet.size();
    }

}
