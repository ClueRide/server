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
package com.clueride.domain.step;

import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Defines what needs to be accomplished to earn this part
 * of the BadgeFeatures.
 */
@Immutable
public class Step {
    private final int id;
    private final String name;
    private final int badgeId;
    private final String badgeName;
    private final int classId;
    private final String className;

    @Inject
    public Step(StepEntity stepEntity) {
        this.id = stepEntity.getId();
        this.name = stepEntity.getName();
        this.badgeId = stepEntity.getBadgeId();
        this.badgeName = stepEntity.getBadgeName();
        this.classId = stepEntity.getClassId();
        this.className = stepEntity.getClassName();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBadgeId() {
        return badgeId;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public int getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
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
