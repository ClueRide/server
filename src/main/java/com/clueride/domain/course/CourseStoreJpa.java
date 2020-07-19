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

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;
import java.io.IOException;
import java.util.List;

/**
 * JPA-based implementation of CourseStore.
 */
public class CourseStoreJpa implements CourseStore {
    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Override
    public Integer addNew(CourseEntity courseEntity) throws IOException {
        try {
            userTransaction.begin();
            entityManager.persist(courseEntity);
            userTransaction.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
            e.printStackTrace();
        }
        return courseEntity.getId();
    }

    @Override
    public CourseEntity getCourseById(Integer id) {
        return entityManager.find(CourseEntity.class, id);
    }

    @Override
    public void update(CourseEntity courseEntity) {
        try {
            userTransaction.begin();
            entityManager.merge(courseEntity);
            userTransaction.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<CourseEntity> getCourses() {
        return entityManager.createQuery(
                "SELECT c from CourseEntity c ORDER BY c.name",
                CourseEntity.class
        ).getResultList();
    }

}
