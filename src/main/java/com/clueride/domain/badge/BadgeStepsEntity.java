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
 * Created by jett on 6/16/19.
 */
package com.clueride.domain.badge;

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

import com.clueride.domain.step.Step;
import com.clueride.domain.step.StepEntity;

/**
 * Properties for a Badge that is earned by meeting
 * a set of Steps.
 */
@Entity
@Table(name = "posts_with_sub_achievements")
public class BadgeStepsEntity {

    @Id
    @Column(name = "parent_id")
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="parent_id", referencedColumnName = "post_id")
    private BadgeFeaturesEntity badgeEntity;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="parent_id")
    private List<StepEntity> childStepEntities;

    public int getId() {
        return id;
    }

    public BadgeFeatures getBadgeFeatures() {
        return badgeEntity.build();
    }

    public List<Step> getChildSteps() {
        List<Step> childSteps = new ArrayList<>();
        for (StepEntity stepEntity : childStepEntities) {
            childSteps.add(stepEntity.build());
        }
        return childSteps;
    }

}
