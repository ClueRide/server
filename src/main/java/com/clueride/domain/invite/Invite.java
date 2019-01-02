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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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
    public Invite(Builder builder) {
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

    @Entity(name="invite")
    public static final class Builder {
        @Id
        @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="invite_pk_sequence")
        @SequenceGenerator(name="invite_pk_sequence",sequenceName="invite_id_seq", allocationSize=1)
        private Integer id;

        @Column(name="member_id")
        private Integer memberId;
        @Column(name="outing_id")
        private Integer outingId;
        @Column(name="team_id")
        private Integer teamId;

        @Column(name="state")
        @Enumerated(EnumType.STRING)
        private InviteState inviteState;

        @Transient private String guideName;
        @Transient private String teamName;
        @Transient private String courseName;
        @Transient private URL courseUrl;
        @Transient private Date scheduledTime;

        public static Builder builder() {
            return new Builder();
        }

        public static Builder from(Invite instance) {
            return builder()
                    .withId(instance.id)
                    .withOutingId(instance.getOutingId())
                    .withMemberId(instance.memberId)
                    .withState(instance.state)
                    .withGuideName(instance.guideName)
                    .withTeamName(instance.teamName)
                    .withCourseName(instance.courseName)
                    .withCourseUrl(instance.courseUrl)
                    .withScheduledTime(instance.scheduledTime)
                    ;
        }

        public Invite build() {
            return new Invite(this);
        }

        public Integer getOutingId() {
            return outingId;
        }

        public Integer getMemberId() {
            return memberId;
        }

        public Builder withMemberId(Integer memberId) {
            this.memberId = memberId;
            return this;
        }

        /* Assists Jackson in creating instances. */
        public Builder withOutingId(Integer outingId) {
            this.outingId = outingId;
            return this;
        }

        public Integer getId() {
            return id;
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Integer getTeamId() {
            return teamId;
        }

        public Builder withTeamId(Integer teamId) {
            this.teamId = teamId;
            return this;
        }

        public InviteState getState() {
            return inviteState;
        }

        public Builder withState(InviteState inviteState) {
            this.inviteState = inviteState;
            return this;
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

        public String getCourseName() {
            return courseName;
        }

        public Builder withCourseName(String courseName) {
            this.courseName = courseName;
            return this;
        }

        public URL getCourseUrl() {
            return courseUrl;
        }

        public Builder withCourseUrl(URL courseUrl) {
            this.courseUrl = courseUrl;
            return this;
        }

        public Date getScheduledTime() {
            return scheduledTime;
        }

        public Builder withScheduledTime(Date scheduledTime) {
            this.scheduledTime = scheduledTime;
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
