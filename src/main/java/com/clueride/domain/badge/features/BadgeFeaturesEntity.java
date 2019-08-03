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
package com.clueride.domain.badge.features;

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
import com.clueride.domain.step.StepEntity;
import static java.util.Objects.requireNonNull;

/**
 * Read-only Entity for {@link BadgeFeatures} instances.
 *
 * This defines the attributes of a Step which either builds
 * to a higher level Achievement, or is comprised of child
 * achievements. The {@link StepEntity} provides this structure
 * relating the `post_id` of these BadgeFeatures instances.
 */
@Entity
@Table(name = "badge_features")
public class BadgeFeaturesEntity {
    @Transient
    private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Id
    @Column(name = "post_id")
    private Integer id;

    @Column(name = "post_title")
    private String badgeDisplayName;

    @Column(name = "base_url")
    private String baseUrlString;

    @Column(name = "image_url")
    private String imageUrlString;

    @Column(name = "badge_name")
    private String badgeName;

    @Column(name = "badge_level")
    private String level;

    @Transient      // Derived from String imageUrlString
    private URL imageUrl;

    public BadgeFeaturesEntity() {}

    public BadgeFeatures build() {
        return new BadgeFeatures(this);
    }

    public static BadgeFeaturesEntity builder() {
        return new BadgeFeaturesEntity();
    }

    public static BadgeFeaturesEntity from(BadgeFeaturesEntity builderToCopy) {
        return builder()
                .withId(builderToCopy.getId())
                .withBadgeName(builderToCopy.getBadgeName())
                .withBaseUrlString(builderToCopy.getBaseUrlString())
                .withImageUrlString(builderToCopy.getImageUrlString());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BadgeFeaturesEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getBadgeDisplayName() {
        return badgeDisplayName;
    }

    public ALevel getLevel() {
        return ALevel.toALevel(this.level);
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

    BadgeFeaturesEntity withImageUrl(URL imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public BadgeFeaturesEntity withImageUrlString(String imageUrlString) {
        this.imageUrlString = imageUrlString;
        return this;
    }

    URL getCriteriaUrl() {
        return criteriaUrlFromBaseAndLevel(
                this.baseUrlString,
                this.badgeName,
                this.level
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

    public BadgeFeaturesEntity withBaseUrlString(String baseUrlString) {
        this.baseUrlString = baseUrlString;
        return this;
    }

    String getBadgeName() {
        return badgeName;
    }

    public BadgeFeaturesEntity withBadgeName(String badgeName) {
        this.badgeName = badgeName;
        return this;
    }

    String getBadgeLevelAsString() {
        return level;
    }

    public BadgeFeaturesEntity withBadgeLevel(String badgeLevel) {
        this.level = badgeLevel;
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
