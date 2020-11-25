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
 * Created by jett on 3/5/16.
 */
package com.clueride.domain.outing;

import com.clueride.domain.course.Course;
import com.clueride.domain.team.Team;

import java.util.Date;

/**
 * Model for the Outing: a particular {@link Team} running a particular {@link Course}
 * at a scheduled time.
 */
public class Outing {
    private final Integer id;
    private Integer teamId;
    private Integer courseId;
    private Date scheduledTime;
    private Integer guideMemberId;

    Outing(OutingViewEntity builder) {
        this.id = builder.getId();
        this.teamId = builder.getTeamId();
        this.courseId = builder.getCourseId();
        this.scheduledTime = builder.getScheduledTime();
        this.guideMemberId = builder.getGuideMemberId();
    }

    Outing(OutingEntity builder) {
        this.id = builder.getId();
        this.teamId = builder.getTeamId();
        this.courseId = builder.getCourseId();
        this.scheduledTime = builder.getScheduledTime();
        this.guideMemberId = builder.getGuideMemberId();
    }

    public Integer getId() {
        return id;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public Date getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(Date scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public Integer getGuideMemberId() {
        return guideMemberId;
    }

}
