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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Builder for Course instances (which are probably CourseView Instances).
 */
@Entity(name="Course")
@Table(name="course")
public class CourseBuilder {
    @Id
    private Integer id;
    private String name;
    private String description;
    @Column(name="course_type_id")
    private Integer courseTypeId;
    private String url;
    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            mappedBy = "course"
    )
    private List<CourseToPathBuilder> courseToPathBuilders = new ArrayList<>();

    @Transient  /* From the CourseToPathBuilders. */
    private List<Integer> pathIds;

    @Transient
    private List<Integer> locationIds;

//    private Location departure;
//    private Location destination;

    public static CourseBuilder builder() {
        return new CourseBuilder();
    }

    public static CourseBuilder from(Course course) {
        return builder()
                .withId(course.getId())
                .withName(course.getName())
                .withDescription(course.getDescription())
                .withCourseTypeId(course.getCourseTypeId())
                ;
    }

    public Course build() {
        this.pathIds = new ArrayList<>();
        for (CourseToPathBuilder builder : courseToPathBuilders) {
            pathIds.add(builder.getPathId());
        }

        return new Course(this);
    }

    public Integer getId() {
        return id;
    }

    public CourseBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CourseBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CourseBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getCourseTypeId() {
        return courseTypeId;
    }

    public CourseBuilder withCourseTypeId(Integer courseTypeId) {
        this.courseTypeId = courseTypeId;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public CourseBuilder withUrl(String url) {
        this.url = url;
        return this;
    }

    public List<CourseToPathBuilder> getCourseToPathBuilders() {
        return courseToPathBuilders;
    }

    public CourseBuilder withCourseToPathBuilders(List<CourseToPathBuilder> courseToPathBuilders) {
        this.courseToPathBuilders = courseToPathBuilders;
        return this;
    }

    public void setCourseToPathBuilders(List<CourseToPathBuilder> courseToPathBuilders) {
        this.courseToPathBuilders = courseToPathBuilders;
    }

    public List<Integer> getPathIds() {
        return pathIds;
    }

    public CourseBuilder withPathIds(List<Integer> pathIds) {
        this.pathIds = pathIds;
        return this;
    }

    public List<Integer> getLocationIds() {
        return locationIds;
    }

    public CourseBuilder withLocationIds(List<Integer> locationIds) {
        this.locationIds = locationIds;
        return this;
    }

}
