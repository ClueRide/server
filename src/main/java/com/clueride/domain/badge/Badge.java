/*
 * Copyright 2018 Jett Marks
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
 * Created by jett on 8/4/18.
 */
package com.clueride.domain.badge;

import java.net.URL;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static java.util.Objects.requireNonNull;

/**
 * Pojo for Badges.
 *
 * Each instance of awarding a badge type is a Badge, so the IDs are
 * unique instances of an awarded badge.
 */
@Immutable
public class Badge {
    private final Integer id;
    private final Integer userId;
    private final BadgeType badgeType;
    private final URL badgeImageUrl;
    private final URL badgeCriteriaUrl;

    public Badge(BadgeBuilder builder) {
        this.id = requireNonNull(builder.getId());
        this.userId = requireNonNull(builder.getUserId());
        this.badgeType = requireNonNull(builder.getBadgeType());
        this.badgeImageUrl = requireNonNull(builder.getImageUrl());
        this.badgeCriteriaUrl = requireNonNull(builder.getCriteriaUrl());
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public BadgeType getBadgeType() {
        return badgeType;
    }

    public URL getBadgeImageUrl() {
        return badgeImageUrl;
    }

    public URL getBadgeCriteriaUrl() {
        return badgeCriteriaUrl;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
