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
 * Created by jett on 1/5/19.
 */
package com.clueride.domain.outing;

import java.net.URL;
import java.util.Date;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.clueride.domain.location.latlon.LatLon;

/**
 * Presentation of an Outing with details joined in from other entities.
 * <ul>
 *     <li>Scheduled Time</li>
 *     <li>Location (lat/lon from the Course)</li>
 *     <li>Course Name (from the Course)</li>
 *     <li>URL of the Course description (from the Course)</li>
 *     <li>Guide's Name (from the Member table)<li>
 *     <li>Team's Name (from the Team)</li>
 * </ul>
 */
@Immutable
public class OutingView {
    private final Integer id;
    private final Date scheduledTime;
    private final Integer startingLocationId;
    private final LatLon startPin;
    private final String courseName;
    private final String courseDescription;
    private final URL courseUrl;
    private final String guideName;
    private final Integer guideMemberId;
    private final Integer teamId;
    private final String teamName;
    private final Integer courseId;

    public OutingView(OutingViewBuilder builder) {
        this.id = builder.getId();
        this.startingLocationId = builder.getStartingLocationId();
        this.scheduledTime = builder.getScheduledTime();
        this.startPin = builder.getStartPin();
        this.courseId = builder.getCourseId();
        this.courseName = builder.getCourseName();
        this.courseDescription = builder.getCourseDescription();
        this.courseUrl = builder.getCourseUrl();
        this.guideName = builder.getGuideName();
        this.guideMemberId = builder.getGuideMemberId();
        this.teamId = builder.getTeamId();
        this.teamName = builder.getTeamName();
    }

    public Integer getId() {
        return id;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public Integer getStartingLocationId() {
        return startingLocationId;
    }

    public LatLon getStartPin() {
        return startPin;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public URL getCourseUrl() {
        return courseUrl;
    }

    public String getGuideName() {
        return guideName;
    }

    public Integer getGuideMemberId() {
        return guideMemberId;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
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
