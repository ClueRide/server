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
 * Created by jett on 8/20/19.
 */
package com.clueride.domain.achievement;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * JPA persistence Entity for Achievements by a user.
 *
 * These are identified by their Step ID to allow matching against Badge Tree.
 */
@Entity
@Table(name = "badge_achievement_by_user")
public class AchievementEntity {
    @Id
    @Column(name = "entry_id")
    private int id;

    @Column(name = "step_id")
    private int stepId;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "step_name")
    private String title;

    @Column(name = "post_type")
    private String postType;

    @Column(name = "date_earned")
    private Date earned;

    public Achievement build() {
        return new Achievement(this);
    }

    public int getId() {
        return id;
    }

    public AchievementEntity withId(int id) {
        this.id = id;
        return this;
    }

    public int getStepId() {
        return stepId;
    }

    public AchievementEntity withStepId(int stepId) {
        this.stepId = stepId;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public AchievementEntity withUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public AchievementEntity withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getPostType() {
        return postType;
    }

    public AchievementEntity withPostType(String postType) {
        this.postType = postType;
        return this;
    }

    public Date getEarned() {
        return earned;
    }

    public AchievementEntity withEarned(Date earned) {
        this.earned = earned;
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
