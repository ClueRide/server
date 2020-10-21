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
package com.clueride.domain.path.attractions;

import com.clueride.domain.path.meta.PathMetaEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * JPA-aware reading of Path information for assembling the data
 * without Geometry for a Course and its locations.
 *
 * This is a read-only entity because it is backed by a view.
 *
 * This class is used to tie together the IDs, but not necessarily
 * the actual entities behind those IDs. It's the responsibility of
 * the Attraction and Course services to fill out those objects.
 */
@Entity
@Table(name="course_path_attraction")
public class CoursePathAttractionsEntity {
    @Id
    private Integer id;
    @Column(name="course_id")
    private Integer courseId;
    @Column(name="path_id")
    private Integer pathId;
    @Column(name="path_order")
    private Integer pathOrder;
    @Column(name="start_node_id")
    private Integer startNodeId;
    @Column(name="end_node_id")
    private Integer endNodeId;
    @Column(name="start_attraction_id")
    private Integer startAttractionId;
    @Column(name="end_attraction_id")
    private Integer endAttractionId;

    public static PathMetaEntity from(CoursePathAttractionsEntity existingPath) {
        return PathMetaEntity.builder()
                .withId(existingPath.pathId)
                .withCourseToPathId(existingPath.id)
                .withStartAttractionId(existingPath.startAttractionId)
                .withEndAttractionId(existingPath.endAttractionId);
    }

    public CoursePathAttractions build() {
        return new CoursePathAttractions(this);
    }

    public Integer getId() {
        return id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public Integer getPathId() {
        return pathId;
    }

    public Integer getPathOrder() {
        return pathOrder;
    }

    public void setPathOrder(Integer pathOrder) {
        this.pathOrder = pathOrder;
    }

    public Integer getStartNodeId() {
        return startNodeId;
    }

    public Integer getEndNodeId() {
        return endNodeId;
    }

    public Integer getStartLocationId() {
        return startAttractionId;
    }

    public Integer getEndLocationId() {
        return endAttractionId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public void setPathId(Integer pathId) {
        this.pathId = pathId;
    }

    public void setStartNodeId(Integer startNodeId) {
        this.startNodeId = startNodeId;
    }

    public void setEndNodeId(Integer endNodeId) {
        this.endNodeId = endNodeId;
    }

    public void setStartAttractionId(Integer startAttractionId) {
        this.startAttractionId = startAttractionId;
    }

    public void setEndAttractionId(Integer endAttractionId) {
        this.endAttractionId = endAttractionId;
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
