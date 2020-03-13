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
 * Created by jett on 5/30/19.
 */
package com.clueride.domain.badge.progress;

import com.clueride.domain.achievement.Achievement;
import com.clueride.domain.badge.features.BadgeFeatures;
import com.clueride.domain.step.Step;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.concurrent.Immutable;
import java.util.List;

/**
 * Carries both what has been achieved and what remains to
 * be achieved for a given user against a given BadgeFeatures.
 *
 * The Steps list out each step that needs to be completed before earning the Badge.
 *
 * The list of Achievements is every step that has been completed.
 *
 * The Progress instance ensures that unique achievements are counted and that
 * each achievement matches with a step.
 */
@Immutable
public class BadgeProgress {
    private final int id;
    private final BadgeFeatures badgeFeatures;
    private final List<Step> steps;
    private final List<Achievement> achievements;
    private final Progress progress;

    public BadgeProgress(BadgeProgressEntity badgeProgressEntity) {
        this.id = badgeProgressEntity.getId();
        this.badgeFeatures = badgeProgressEntity.getBadgeFeatures().build();
        this.achievements = badgeProgressEntity.getAchievements();
        this.steps = badgeProgressEntity.getSteps();
        this.progress = badgeProgressEntity.getProgress();
    }

    public int getId() {
        return id;
    }

    public BadgeFeatures getBadgeFeatures() {
        return badgeFeatures;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    public Progress getProgress() {
        return progress;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
