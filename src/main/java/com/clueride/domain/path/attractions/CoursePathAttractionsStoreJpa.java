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

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * JPA-implementation of {@link CoursePathAttractionsStore}.
 */
public class CoursePathAttractionsStoreJpa implements CoursePathAttractionsStore {
    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Override
    public List<CoursePathAttractionsEntity> getPathAttractionsForCourse(Integer courseId) {
        List<CoursePathAttractionsEntity> builders;
        builders = entityManager.createQuery(
               "SELECT p FROM CoursePathAttractionsEntity p " +
                       " WHERE p.courseId = :courseId" +
                       " ORDER BY p.pathOrder",
                CoursePathAttractionsEntity.class
        ).setParameter(
                "courseId", courseId
        ).getResultList();

        return builders;
    }

    @Nullable
    @Override
    public CoursePathAttractionsEntity findSuitablePath(Integer startId, Integer endId) {
        List<CoursePathAttractionsEntity> builders;
        builders = entityManager.createQuery(
                "SELECT p FROM CoursePathAttractionsEntity p " +
                        " WHERE p.startAttractionId = :startId" +
                        "   AND p.endAttractionId = :endId",
                CoursePathAttractionsEntity.class
        ).setParameter(
                "startId", startId
        ).setParameter(
                "endId", endId
        ).getResultList();

        if (builders.size() > 0) {
            return builders.get(0);
        } else {
            return null;
        }

    }

}
