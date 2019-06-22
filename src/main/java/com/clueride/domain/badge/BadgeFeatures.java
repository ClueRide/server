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

import com.clueride.domain.achievement.ALevel;
import static java.util.Objects.requireNonNull;

/**
 * Pojo for BadgeFeatures.
 */
@Immutable
public class BadgeFeatures {
    private final Integer id;
    private final String displayName;
    private final ALevel level;
    private final URL imageUrl;
    private final URL criteriaUrl;

    public BadgeFeatures(BadgeFeaturesEntity builder) {
        this.id = requireNonNull(builder.getId());
        this.displayName = requireNonNull(builder.getBadgeDisplayName());
        this.level = requireNonNull(builder.getLevel());
        this.imageUrl = requireNonNull(builder.getImageUrl());
        this.criteriaUrl = requireNonNull(builder.getCriteriaUrl());
    }

    public Integer getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ALevel getLevel() {
        return level;
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public URL getCriteriaUrl() {
        return criteriaUrl;
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
