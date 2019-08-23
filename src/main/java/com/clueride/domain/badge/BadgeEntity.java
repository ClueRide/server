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
 * Created by jett on 8/22/19.
 */
package com.clueride.domain.badge;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.clueride.domain.badge.features.BadgeFeaturesEntity;

/**
 * JPA Entity for Completed Badge instances.
 */
@Entity
@Table(name="completed_badge")
public class BadgeEntity {
    @Id
    private int id;

    @Column(name = "user_id")
    private int userId;     // Word Press ID

    @OneToOne
    @JoinColumn(name = "badge_id", referencedColumnName = "post_id")
    private BadgeFeaturesEntity badgeFeaturesEntity;

    @Column(name = "date_earned")
    private Date awardedDate;

    public Badge build() {
        return new Badge(this);
    }

    public int getId() {
        return id;
    }

    public BadgeEntity withId(int id) {
        this.id = id;
        return this;
    }

    public int getUserId() {
        return userId;
    }

    public BadgeEntity withUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public BadgeFeaturesEntity getBadgeFeaturesEntity() {
        return badgeFeaturesEntity;
    }

    public BadgeEntity withBadgeFeatures(BadgeFeaturesEntity badgeFeatures) {
        this.badgeFeaturesEntity = badgeFeatures;
        return this;
    }

    public Date getAwardedDate() {
        return awardedDate;
    }

    public BadgeEntity withAwardedDate(Date awardedDate) {
        this.awardedDate = awardedDate;
        return this;
    }

}
