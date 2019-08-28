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
package com.clueride.domain.invite;

import java.net.URL;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Persistable Builder for {@link Invite} instances.
 */
@Entity
@Table(name = "invite")
public final class InviteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invite_pk_sequence")
    @SequenceGenerator(name = "invite_pk_sequence", sequenceName = "invite_id_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "member_id") private Integer memberId;
    @Column(name = "outing_id") private Integer outingId;
    @Column(name = "team_id") private Integer teamId;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private InviteState inviteState;

    @Transient private String guideName;
    @Transient private String teamName;
    @Transient private String courseName;
    @Transient private URL courseUrl;
    @Transient private Date scheduledTime;

    public static InviteEntity builder() {
        return new InviteEntity();
    }

    public static InviteEntity from(Invite instance) {
        return builder()
                .withId(instance.getId())
                .withOutingId(instance.getOutingId())
                .withMemberId(instance.getMemberId())
                .withState(instance.getState())
                .withGuideName(instance.getGuideName())
                .withTeamName(instance.getTeamName())
                .withCourseName(instance.getCourseName())
                .withCourseUrl(instance.getCourseUrl())
                .withScheduledTime(instance.getScheduledTime())
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

    public InviteEntity withMemberId(Integer memberId) {
        this.memberId = memberId;
        return this;
    }

    /* Assists Jackson in creating instances. */
    public InviteEntity withOutingId(Integer outingId) {
        this.outingId = outingId;
        return this;
    }

    public Integer getId() {
        return id;
    }

    public InviteEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public InviteEntity withTeamId(Integer teamId) {
        this.teamId = teamId;
        return this;
    }

    public InviteState getState() {
        return inviteState;
    }

    public InviteEntity withState(InviteState inviteState) {
        this.inviteState = inviteState;
        return this;
    }

    public String getGuideName() {
        return guideName;
    }

    public InviteEntity withGuideName(String guideName) {
        this.guideName = guideName;
        return this;
    }

    public String getTeamName() {
        return teamName;
    }

    public InviteEntity withTeamName(String teamName) {
        this.teamName = teamName;
        return this;
    }

    public String getCourseName() {
        return courseName;
    }

    public InviteEntity withCourseName(String courseName) {
        this.courseName = courseName;
        return this;
    }

    public URL getCourseUrl() {
        return courseUrl;
    }

    public InviteEntity withCourseUrl(URL courseUrl) {
        this.courseUrl = courseUrl;
        return this;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public InviteEntity withScheduledTime(Date scheduledTime) {
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
