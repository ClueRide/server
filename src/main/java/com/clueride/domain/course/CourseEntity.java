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

import com.clueride.domain.course.link.CourseToPathLinkEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder for Course instances (which are probably CourseView Instances).
 */
@Entity
@Table(name="course")
public class CourseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "course_pk_sequence")
    @SequenceGenerator(name="course_pk_sequence", sequenceName = "course_id_seq", allocationSize = 1)
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
    private List<CourseToPathLinkEntity> courseToPathEntities = new ArrayList<>();

    @Column(name="starting_location_id")
    private Integer startingAttractionId;

    @Transient  /* From the CourseToPathBuilders. */
    private List<Integer> pathIds;

    @Transient
    private List<Integer> locationIds;

//    private Location departure;
//    private Location destination;

    public static CourseEntity builder() {
        return new CourseEntity();
    }

    public static CourseEntity from(Course course) {
        return builder()
                .withId(course.getId())
                .withName(course.getName())
                .withDescription(course.getDescription())
                .withCourseTypeId(course.getCourseTypeId())
                ;
    }

    public Course build() {
        this.pathIds = new ArrayList<>();
        for (CourseToPathLinkEntity builder : courseToPathEntities) {
            pathIds.add(builder.getPathId());
        }

        return new Course(this);
    }

    public Integer getId() {
        return id;
    }

    public CourseEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CourseEntity withName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public CourseEntity withDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getCourseTypeId() {
        return courseTypeId;
    }

    public CourseEntity withCourseTypeId(Integer courseTypeId) {
        this.courseTypeId = courseTypeId;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public CourseEntity withUrl(String url) {
        this.url = url;
        return this;
    }

    public List<CourseToPathLinkEntity> getCourseToPathEntities() {
        return courseToPathEntities;
    }

    public CourseEntity withCourseToPathEntities(List<CourseToPathLinkEntity> courseToPathEntities) {
        this.courseToPathEntities = courseToPathEntities;
        return this;
    }

    public CourseEntity withCourseToPathEntity(CourseToPathLinkEntity courseToPathLinkEntity) {
        this.courseToPathEntities.add(courseToPathLinkEntity);
        return this;
    }

    public void setCourseToPathEntities(List<CourseToPathLinkEntity> courseToPathEntities) {
        this.courseToPathEntities = courseToPathEntities;
    }

    public List<Integer> getPathIds() {
        return pathIds;
    }

    public CourseEntity withPathIds(List<Integer> pathIds) {
        this.pathIds = pathIds;
        return this;
    }

    public List<Integer> getLocationIds() {
        return locationIds;
    }

    public CourseEntity withLocationIds(List<Integer> locationIds) {
        this.locationIds = locationIds;
        return this;
    }

    public Integer getStartingAttractionId() {
        return startingAttractionId;
    }

    public CourseEntity withStartingAttractionId(Integer startingAttractionId) {
        this.startingAttractionId = startingAttractionId;
        return this;
    }
}
