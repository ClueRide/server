/*
 * Copyright 2020 Jett Marks
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
 * Created by jett on 11/24/20.
 */
package com.clueride.domain.outing;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Persistable Entity for {@link Outing} instances.
 */
@Entity
@Table(name = "outing")
public class OutingEntity {
    @Id
    private Integer id;

    @Column(name = "scheduled_time") private Date scheduledTime;
    @Column(name = "course_id") private Integer courseId;
    @Column(name = "guide_id") private Integer guideMemberId;
    @Column(name = "team_id") private Integer teamId;

    public OutingEntity() {}

    public static OutingEntity builder() {
        return new OutingEntity();
    }

    public Outing build() {
        return new Outing(this);
    }

    public static OutingEntity from(Outing instance) {
        return builder()
                .withId(instance.getId())
                .withCourseId(instance.getCourseId())
                .withTeamId(instance.getTeamId())
                .withScheduledTime(instance.getScheduledTime())
                .withGuideMemberId(instance.getGuideMemberId())
                ;
    }

    public Integer getId() {
        return id;
    }

    public OutingEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public OutingEntity withScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
        return this;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public OutingEntity withCourseId(Integer courseId) {
        this.courseId = courseId;
        return this;
    }

    public OutingEntity withTeamId(Integer teamId) {
        this.teamId = teamId;
        return this;
    }

    public Integer getGuideMemberId() {
        return guideMemberId;
    }

    public OutingEntity withGuideMemberId(Integer guideMemberId) {
        this.guideMemberId = guideMemberId;
        return this;
    }

    public Integer getTeamId() {
        return teamId;
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
