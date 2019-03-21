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
 * Created by jett on 3/19/19.
 */
package com.clueride.domain.course;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entity representing records in the Course To Path relationship table.
 */
@Entity(name="courseToPath")
@Table(name="course_to_path")
public class CourseToPathBuilder {
    @Id
    private Integer id;
    @Column(name="path_order")
    private Integer pathOrder;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="course_id")
    private CourseBuilder course;

    @Column(name="path_id")
    private Integer pathId;

    public Integer getId() {
        return id;
    }

    public CourseToPathBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getPathOrder() {
        return pathOrder;
    }

    public CourseToPathBuilder withPathOrder(Integer pathOrder) {
        this.pathOrder = pathOrder;
        return this;
    }

    public CourseBuilder getCourse() {
        return course;
    }

    public CourseToPathBuilder withCourse(CourseBuilder course) {
        this.course = course;
        return this;
    }

    public Integer getPathId() {
        return pathId;
    }

    public CourseToPathBuilder withPathId(Integer pathId) {
        this.pathId = pathId;
        return this;
    }

}
