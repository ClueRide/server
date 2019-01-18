/*
 * Copyright 2016 Jett Marks
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
 * Created by jett on 5/29/16.
 */
package com.clueride.domain.invite;

import java.net.URL;
import java.util.Date;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Maps a given Member to a given Outing along with a unique token that serves as the first step
 * toward authenticating that member.
 */
@Immutable
public class Invite {
    private Integer memberId;
    private Integer outingId;
    private Integer id = null;
    private InviteState state;

    /* Derived Fields */
    private String guideName;
    private String teamName;
    private String courseName;
    private URL courseUrl;
    private Date scheduledTime;

    /**
     * Builds immutable instance of Invite from Builder instance.
     *
     * @param builder - Builder with all the data needed to construct Invite instance.
     */
    public Invite(InviteBuilder builder) {
        this.id = builder.getId();
        this.outingId = builder.getOutingId();
        this.memberId = builder.getMemberId();
        this.state = (builder.getState() != null) ? builder.getState() : InviteState.PROVISIONAL;
        this.guideName = builder.getGuideName();
        this.teamName = builder.getTeamName();
        this.courseName = builder.getCourseName();
        this.courseUrl = builder.getCourseUrl();
        this.scheduledTime = builder.getScheduledTime();
    }

    public Integer getMemberId() {
        return memberId;
    }

    public Integer getOutingId() {
        return outingId;
    }

    public Integer getId() {
        return id;
    }

    public InviteState getState() {
        return state;
    }

    public String getGuideName() {
        return guideName;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getCourseName() {
        return courseName;
    }

    public URL getCourseUrl() {
        return courseUrl;
    }

    public Date getScheduledTime() {
        return scheduledTime;
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
