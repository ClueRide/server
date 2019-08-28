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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * JPA-implementation of {@link PathForCourseStore}.
 */
public class PathForCourseStoreJpa implements PathForCourseStore {
    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Override
    public List<PathForCourseEntity> getPathsForCourse(Integer courseId) {
        List<PathForCourseEntity> builders;
        builders = entityManager.createQuery(
               "SELECT p FROM PathForCourseEntity p " +
                       " WHERE p.courseId = :courseId" +
                       " ORDER BY p.pathOrder"
        ).setParameter(
                "courseId", courseId
        ).getResultList();

        return builders;
    }
}
