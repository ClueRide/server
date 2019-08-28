/*
 * Copyright 2018 Jett Marks
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
 * Created by jett on 9/15/18.
 */
package com.clueride.domain.course;

import java.util.List;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.clueride.domain.game.GameState;

/**
 * Contains static information about a Course; persistable to DB and unchanging
 * over the time frame of an Outing.
 *
 * This serves as a reference to obtain information about the structure of the
 * course. The dynamic state information is held in the {@link GameState} instance.
 */
@Immutable
public class Course {
    private final Integer id;
    private final String name;
    private final String description;
    private final String url;
    private final Integer courseTypeId;
    private final List<Integer> pathIds;
    private final List<Integer> locationIds;
//    private final Location departure;
//    private final Location destination;

    public Course(CourseEntity builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.description = builder.getDescription();
        this.url = builder.getUrl();
        this.courseTypeId = builder.getCourseTypeId();
        this.pathIds = builder.getPathIds();
        this.locationIds = builder.getLocationIds();
//        this.departure = builder.getDeparture();
//        this.destination = builder.getDestination();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public Integer getCourseTypeId() {
        return courseTypeId;
    }

    public List<Integer> getPathIds() {
        return pathIds;
    }

    public List<Integer> getLocationIds() {
        return locationIds;
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
