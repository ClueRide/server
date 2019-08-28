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
package com.clueride.domain.outing;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.clueride.domain.location.latlon.LatLonEntity;

/**
 * Persistable Entity for {@link OutingView} instances.
 */
@Entity
@Table(name = "outing_view")
public class OutingViewEntity {
    @Id
    private Integer id;

    @Column(name = "scheduled_time") private Date scheduledTime;
    @Column(name = "course_name") private String courseName;
    @Column(name = "course_id") private Integer courseId;
    @Column(name = "course_description") private String courseDescription;
    @Column(name = "course_url") private String courseUrlAsString;
    @Column(name = "guide_name") private String guideName;
    @Column(name = "guide_id") private Integer guideMemberId;
    @Column(name = "team_name") private String teamName;
    @Column(name = "team_id") private Integer teamId;
    @Column(name = "location_id") private Integer startingLocationId;
    @Column(name = "node_id") private Integer nodeId;
    @Column(name = "lat") private Double lat;
    @Column(name = "lon") private Double lon;

    @Transient
    private URL courseUrl;
    @Transient
    private LatLonEntity startPin = new LatLonEntity();

    public OutingViewEntity() {}

    public static OutingViewEntity builder() {
        return new OutingViewEntity();
    }

    public OutingView build() {
        return new OutingView(this);
    }

    public static OutingViewEntity from(OutingView instance) {
        return builder()
                .withId(instance.getId())
                .withScheduledTime(instance.getScheduledTime())
                .withStartPin(instance.getStartPin())
                .withCourseName(instance.getCourseName())
                .withCourseDescription(instance.getCourseDescription())
                .withCourseUrl(instance.getCourseUrl())
                .withGuideName(instance.getGuideName())
                .withGuideMemberId(instance.getGuideMemberId())
                .withTeamName(instance.getTeamName())
                ;
    }

    public Integer getId() {
        return id;
    }

    public OutingViewEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public OutingViewEntity withScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
        return this;
    }

    public LatLonEntity getStartPin() {
        startPin.setId(nodeId);
        startPin.setLat(lat);
        startPin.setLon(lon);
        return startPin;
    }

    public OutingViewEntity withStartPin(LatLonEntity startPin) {
        this.startPin = startPin;
        return this;
    }

    public String getCourseName() {
        return courseName;
    }

    public OutingViewEntity withCourseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public OutingViewEntity withCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
        return this;
    }

    public URL getCourseUrl() {
        urlFromString(courseUrlAsString);
        return courseUrl;
    }

    public OutingViewEntity withCourseUrl(URL courseUrl) {
        this.courseUrl = courseUrl;
        return this;
    }

    public String getCourseUrlAsString() {
        return courseUrlAsString;
    }

    public OutingViewEntity withCourseUrlAsString(String courseUrlAsString) {
        this.courseUrlAsString = courseUrlAsString;
        urlFromString(courseUrlAsString);
        return this;
    }

    private void urlFromString(String courseUrlAsString) {
        try {
            this.courseUrl = new URL(courseUrlAsString);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Unexpected URL string: " + courseUrlAsString, e);
        }
    }

    public String getGuideName() {
        return guideName;
    }

    public OutingViewEntity withGuideName(String guideName) {
        this.guideName = guideName;
        return this;
    }

    public Integer getGuideMemberId() {
        return guideMemberId;
    }

    public OutingViewEntity withGuideMemberId(Integer guideMemberId) {
        this.guideMemberId = guideMemberId;
        return this;
    }

    public String getTeamName() {
        return teamName;
    }

    public OutingViewEntity withTeamName(String teamName) {
        this.teamName = teamName;
        return this;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public Integer getStartingLocationId() {
        return startingLocationId;
    }

    public OutingViewEntity withStartingLocationId(Integer startingLocationId) {
        this.startingLocationId = startingLocationId;
        return this;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public OutingViewEntity withNodeId(Integer nodeId) {
        this.nodeId = nodeId;
        startPin.setId(nodeId);
        return this;
    }

    public Double getLat() {
        return lat;
    }

    public OutingViewEntity withLat(Double lat) {
        this.lat = lat;
        startPin.setLat(lat);
        return this;
    }

    public Double getLon() {
        return lon;
    }

    public OutingViewEntity withLon(Double lon) {
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
