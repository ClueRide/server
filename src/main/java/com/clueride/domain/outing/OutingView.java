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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

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
    private final LatLon startPin;
    private final String courseName;
    private final String courseDescription;
    private final URL courseUrl;
    private final String guideName;
    private final String teamName;

    public OutingView(Builder builder) {
        this.id = builder.getId();
        this.scheduledTime = builder.getScheduledTime();
        this.startPin = builder.getStartPin();
        this.courseName = builder.getCourseName();
        this.courseDescription = builder.getCourseDescription();
        this.courseUrl = builder.getCourseUrl();
        this.guideName = builder.getGuideName();
        this.teamName = builder.getTeamName();
    }

    public Integer getId() {
        return id;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public LatLon getStartPin() {
        return startPin;
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

    @Entity(name="outing_view")
    public static final class Builder {
        @Id
        private Integer id;

        @Column(name="scheduled_time") private Date scheduledTime;
        @Column(name="course_name") private String courseName;
        @Column(name="course_description") private String courseDescription;
        @Column(name="course_url") private String courseUrlAsString;
        @Column(name="guide_name") private String guideName;
        @Column(name="team_name") private String teamName;
        @Column(name="node_id") private Integer nodeId;
        @Column(name="lat") private Double lat;
        @Column(name="lon") private Double lon;

        @Transient
        private URL courseUrl;
        @Transient
        private LatLon startPin = new LatLon();

        public static Builder builder() {
            return new Builder();
        }

        public OutingView build() {
            return new OutingView(this);
        }

        public static Builder from(OutingView instance) {
            return builder()
                    .withId(instance.getId())
                    .withScheduledTime(instance.getScheduledTime())
                    .withStartPin(instance.getStartPin())
                    .withCourseName(instance.getCourseName())
                    .withCourseDescription(instance.getCourseDescription())
                    .withCourseUrl(instance.getCourseUrl())
                    .withGuideName(instance.getGuideName())
                    .withTeamName(instance.getTeamName())
                    ;
        }

        public Integer getId() {
            return id;
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Date getScheduledTime() {
            return scheduledTime;
        }

        public Builder withScheduledTime(Date scheduledTime) {
            this.scheduledTime = scheduledTime;
            return this;
        }

        public LatLon getStartPin() {
            startPin.setId(nodeId);
            startPin.setLat(lat);
            startPin.setLon(lon);
            return startPin;
        }

        public Builder withStartPin(LatLon startPin) {
            this.startPin = startPin;
            return this;
        }

        public String getCourseName() {
            return courseName;
        }

        public Builder withCourseName(String courseName) {
            this.courseName = courseName;
            return this;
        }

        public String getCourseDescription() {
            return courseDescription;
        }

        public Builder withCourseDescription(String courseDescription) {
            this.courseDescription = courseDescription;
            return this;
        }

        public URL getCourseUrl() {
            urlFromString(courseUrlAsString);
            return courseUrl;
        }

        public Builder withCourseUrl(URL courseUrl) {
            this.courseUrl = courseUrl;
            return this;
        }

        public String getCourseUrlAsString() {
            return courseUrlAsString;
        }

        public Builder withCourseUrlAsString(String courseUrlAsString) {
            this.courseUrlAsString = courseUrlAsString;
            urlFromString(courseUrlAsString);
            return this;
        }

        private void urlFromString(String courseUrlAsString) {
            try {
                this.courseUrl = new URL(courseUrlAsString);
            } catch (MalformedURLException e) {
                throw new RuntimeException("Unexpected URL string: " + courseUrlAsString, e );
            }
        }

        public String getGuideName() {
            return guideName;
        }

        public Builder withGuideName(String guideName) {
            this.guideName = guideName;
            return this;
        }

        public String getTeamName() {
            return teamName;
        }

        public Builder withTeamName(String teamName) {
            this.teamName = teamName;
            return this;
        }

        public Integer getNodeId() {
            return nodeId;
        }

        public Builder withNodeId(Integer nodeId) {
            this.nodeId = nodeId;
            startPin.setId(nodeId);
            return this;
        }

        public Double getLat() {
            return lat;
        }

        public Builder withLat(Double lat) {
            this.lat = lat;
            startPin.setLat(lat);
            return this;
        }

        public Double getLon() {
            return lon;
        }

        public Builder withLon(Double lon) {
            this.lon = lon;
            startPin.setLon(lon);
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

}
