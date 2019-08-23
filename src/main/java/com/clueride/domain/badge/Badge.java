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

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.clueride.domain.badge.features.BadgeFeatures;

/**
 * Represents a completed and awarded Badge.
 */
@Immutable
public class Badge {
    private int id;
    private int userId;     // Word Press ID
    private BadgeFeatures badgeFeatures;
    private Date awardedDate;

    public Badge(BadgeEntity badgeEntity) {
        this.id = badgeEntity.getId();
        this.userId = badgeEntity.getUserId();
        this.badgeFeatures = badgeEntity.getBadgeFeaturesEntity().build();
        this.awardedDate = badgeEntity.getAwardedDate();
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public BadgeFeatures getBadgeFeatures() {
        return badgeFeatures;
    }

    public Date getAwardedDate() {
        return awardedDate;
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
