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
 * Created by jett on 1/27/19.
 */
package com.clueride.domain.path;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;

/**
 * The data for a Path which links a Course to its Locations.
 */
@Immutable
public class PathForCourse implements Path {
    private final Integer id;
    private final Integer courseId;
    private final Integer startNodeId;
    private final Integer startLocationId;
    private final Integer endNodeId;
    private final Integer endLocationId;

    public PathForCourse(PathForCourseEntity pathForCourseEntity) {
        this.id = pathForCourseEntity.getId();
        this.courseId = pathForCourseEntity.getCourseId();
        this.startNodeId = pathForCourseEntity.getStartNodeId();
        this.startLocationId = pathForCourseEntity.getStartLocationId();
        this.endNodeId = pathForCourseEntity.getEndNodeId();
        this.endLocationId = pathForCourseEntity.getEndLocationId();
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public Integer getCourseId() {
        return courseId;
    }

    @Override
    public Integer getStartNodeId() {
        return this.startNodeId;
    }

    @Override
    public Integer getEndNodeId() {
        return this.endNodeId;
    }

    @Override
    public List<Integer> getEdgeIds() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Integer getStartAttractionId() {
        return this.startLocationId;
    }

    @Override
    public Integer getEndAttractionId() {
        return this.endLocationId;
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
