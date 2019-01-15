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
 * Created by jett on 1/13/19.
 */
package com.clueride.domain.badge;

import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clueride.MalformedUrlWithinDBException;
import static java.util.Objects.requireNonNull;

/**
 * Persistable Builder for {@link Badge} instances.
 */
@Entity(name = "badge_display_per_user")
@Table(name = "badge_display_per_user")
public class BadgeBuilder {
    @Transient
    private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Id
    @Column(name = "achievement_id")
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "base_url")
    private String baseUrlString;

    @Column(name = "image_url")
    private String imageUrlString;

    @Column(name = "badge_name")
    private String badgeName;

    @Column(name = "badge_level")
    private String badgeLevel;

    @Transient
    private BadgeType badgeType;

    @Transient
    private URL imageUrl;

    public BadgeBuilder() {}

    public Badge build() {
        return new Badge(this);
    }

    public static BadgeBuilder builder() {
        return new BadgeBuilder();
    }

    public static BadgeBuilder from(BadgeBuilder builderToCopy) {
        return builder()
                .withId(builderToCopy.getId())
                .withUserId(builderToCopy.getUserId())
                .withBadgeName(builderToCopy.getBadgeName())
                .withBadgeLevel(builderToCopy.getBadgeLevel())
                .withBadgeType(builderToCopy.getBadgeType())
                .withBaseUrlString(builderToCopy.getBaseUrlString())
                .withImageUrlString(builderToCopy.getImageUrlString());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BadgeBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return userId;
    }

    public BadgeBuilder withUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public BadgeType getBadgeType() {
        return badgeType;
    }

    public BadgeBuilder withBadgeType(BadgeType badgeType) {
        this.badgeType = badgeType;
        return this;
    }

    public BadgeBuilder withBadgeTypeString(String badgeTypeString) {
        this.badgeType = BadgeType.valueOf(badgeTypeString);
        return this;
    }

    public URL getImageUrl() {
        try {
            this.imageUrl = new URL(this.imageUrlString);
        } catch (MalformedURLException e) {
            throw new MalformedUrlWithinDBException(e);
        }
        return imageUrl;
    }

    public String getImageUrlString() {
        return imageUrlString;
    }

    BadgeBuilder withImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public BadgeBuilder withImageUrlString(String imageUrlString) {
        this.imageUrlString = imageUrlString;
        return this;
    }

    URL getCriteriaUrl() {
        return criteriaUrlFromBaseAndLevel(
                this.baseUrlString,
                this.badgeName,
                this.badgeLevel
        );
    }

    /**
     * Constructs the Criteria Page URL from elements available from BadgeOS.
     *
     * @param baseUrlString The URL of the BadgeOS system's encompassing post.
     * @param badgeName     Specific name for the class of badge.
     * @param badgeLevel    Level of badge within the class -- matches the "slug" for the badge's page.
     * @return Assembled URL containing elements that make up the badge's criteria page.
     */
    private URL criteriaUrlFromBaseAndLevel(
            String baseUrlString,
            String badgeName,
            String badgeLevel
    ) {
        requireNonNull(baseUrlString, "Base URL is null");
        requireNonNull(badgeName, "Badge Name is null");
        requireNonNull(badgeLevel, "Badge Level is null");

        URL baseUrl;
        try {
            baseUrl = new URL(baseUrlString);
            StringBuilder urlString = new StringBuilder()
                    .append(baseUrl.getProtocol())
                    .append("://")
                    .append(baseUrl.getHost())
                    .append("/")
                    .append(badgeName)
                    .append("/")
                    .append(badgeLevel);
            LOGGER.trace("Constructed URL String: " + urlString.toString());
            return new URL(urlString.toString());
        } catch (MalformedURLException e) {
            LOGGER.error("Unable to assemble URL:" + e.getMessage());
            throw new MalformedUrlWithinDBException(e);
        }
    }

    String getBaseUrlString() {
        return baseUrlString;
    }

    public BadgeBuilder withBaseUrlString(String baseUrlString) {
        this.baseUrlString = baseUrlString;
        return this;
    }

    String getBadgeName() {
        return badgeName;
    }

    public BadgeBuilder withBadgeName(String badgeName) {
        this.badgeName = badgeName;
        return this;
    }

    String getBadgeLevel() {
        return badgeLevel;
    }

    public BadgeBuilder withBadgeLevel(String badgeLevel) {
        this.badgeLevel = badgeLevel;
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
