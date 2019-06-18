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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.clueride.domain.achievement.Achievement;
import com.clueride.domain.badge.BadgeFeaturesEntity;
import com.clueride.domain.step.Step;
import com.clueride.domain.step.StepEntity;
import static java.util.Objects.requireNonNull;

/**
 * Read-only instance of {@link BadgeProgress} with the exception of
 * the Achievements which cannot be directly accessed via table relationships.
 */
@Entity
@Table(name = "badge_features")
public class BadgeProgressEntity {
    @Id
    @Column(name = "post_id")
    private int id;

    @OneToOne
    @JoinColumn(name="post_id")
    private BadgeFeaturesEntity badgeFeatures;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id")
    private List<StepEntity> stepEntities;

    @Transient
    /* Not modeled by table relationships. */
    private List<Achievement> achievements;

    @Transient
    /* Filled in by the Service layer. */
    private Progress progress;

    /**
     * Instantiates immutable instance from this instance's properties.
     * @return Instance of BadgeProgress.
     */
    public BadgeProgress build() {
        return new BadgeProgress(this);
    }

    /**
     * Matches the Post ID which is also the Badge ID.
     * @return Unique Identifier for the Badge.
     */
    public int getId() {
        return id;
    }

    /**
     * Attributes of the Badge useful for presentation.
     * @return BadgeFeatures instance.
     */
    public BadgeFeaturesEntity getBadgeFeatures() {
        return badgeFeatures;
    }

    /**
     * Definition of the criteria for earning the badge.
     * @return List of Steps for the Badge to be earned.
     */
    public List<Step> getSteps() {
        List<Step> steps = new ArrayList<>();
        for (StepEntity entity : this.stepEntities) {
            steps.add(entity.build());
        }
        return steps;
    }

    /**
     * Allows adding the list of completed steps -- which
     * isn't part of the BadgeOS's relational table structure.
     * @param achievements
     * @return this instance for chaining.
     */
    public BadgeProgressEntity withAchievements(List<Achievement> achievements) {
        this.achievements = achievements;
        return this;
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    /**
     * Constructed from the list of Steps and Achievements.
     * @return Progress instance.
     */
    public Progress getProgress() {
        return new Progress(
                getSteps(),
                requireNonNull(achievements, "set the achievements before building")
        );
    }

}
