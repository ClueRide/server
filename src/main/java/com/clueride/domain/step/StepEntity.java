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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Persistence Entity for the Step class.
 *
 * It is here that we see the three separate posts that
 * come together:
 * <ul>
 *  <li>A Badge</li>
 *  <li>A Step instance which can be awarded</li>
 *  <li>A "Sub"-Badge that rolls up step instances.</li>
 * </ul>
 *
 * This is a Read-only Entity based on a View.
 */
@Entity
@Table(name = "step_class_rollup")
public class StepEntity {

    @Id
    @Column(name = "step_id")
    private int id;

    @Column(name = "step_name")
    private String name;

    @Column(name = "badge_id")
    private int badgeId;

    @Column(name = "badge_name")
    private String badgeName;

    @Column(name = "class_id")
    private int classId;

    @Column(name = "class_name")
    private String className;

    public Step build() {
        return new Step(this);
    }

    public int getId() {
        return id;
    }

    public StepEntity withId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public StepEntity withName(String name) {
        this.name = name;
        return this;
    }

    public int getBadgeId() {
        return badgeId;
    }

    public StepEntity withBadgeId(int badgeId) {
        this.badgeId = badgeId;
        return this;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public StepEntity withBadgeName(String badgeName) {
        this.badgeName = badgeName;
        return this;
    }

    public int getClassId() {
        return classId;
    }

    public StepEntity withClassId(int classId) {
        this.classId = classId;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public StepEntity withClassName(String className) {
        this.className = className;
        return this;
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
