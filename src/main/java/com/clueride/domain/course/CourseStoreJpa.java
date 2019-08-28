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

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * JPA-based implementation of CourseStore.
 */
public class CourseStoreJpa implements CourseStore {
    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Override
    public Integer addNew(CourseEntity courseEntity) throws IOException {
        return null;
    }

    @Override
    public CourseEntity getCourseById(Integer id) {
        return entityManager.find(CourseEntity.class, id);
    }

    @Override
    public void update(CourseEntity courseEntity) {

    }

    @Override
    public List<CourseEntity> getCourses() {
        return null;
    }

}
