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
 * Created by jett on 1/26/19.
 */
package com.clueride.domain.course;

import com.clueride.auth.Secured;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * REST API for {@link Course} operations.
 */
@Path("/course")
public class CourseWebService {
    @Inject
    private CourseService courseService;

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GET
    @Secured
    @Path("active")
    @Produces(MediaType.APPLICATION_JSON)
    public Course getSessionCourse() {
        return courseService.getSessionCourse();
    }

    @GET
    @Secured
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Course getById(@PathParam("id") Integer courseId) {
        return courseService.getById(courseId);
    }

    @PUT
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Course addNewCourse(CourseEntity courseEntity) {
        return courseService.addNewCourse(courseEntity);
    }

    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Course updateCourse(CourseEntity courseEntity) {
        return courseService.updateCourse(courseEntity);
    }

}
