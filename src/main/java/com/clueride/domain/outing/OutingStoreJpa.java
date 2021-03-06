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
 * Created by jett on 1/1/19.
 */
package com.clueride.domain.outing;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

/**
 * JPA-based implementation of OutingStore.
 */
public class OutingStoreJpa implements OutingStore {
    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Override
    public Integer addNew(OutingViewEntity entity) throws IOException {
        return null;
    }

    @Override
    public OutingViewEntity getOutingById(Integer outingId) {
        requireNonNull(outingId, "Must specify an outing ID");
        return entityManager.find(OutingViewEntity.class, outingId);
    }

    @Override
    public OutingViewEntity getOutingViewById(Integer outingId) {
        requireNonNull(outingId, "Must specify an outing ID");
        return entityManager.find(OutingViewEntity.class, outingId);
    }

    @Override
    public OutingViewEntity setCourseForEternalOuting(Integer courseId) {
        requireNonNull(courseId, "Must specify a course ID");

        OutingEntity outingEntity = entityManager.find(
                OutingEntity.class,
                OutingConstants.ETERNAL_OUTING_ID
        );

        outingEntity.withCourseId(courseId);

        try {
            userTransaction.begin();
            entityManager.merge(outingEntity);
            userTransaction.commit();
        } catch (NotSupportedException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SystemException e) {
            e.printStackTrace();
        }

        return entityManager.find(
                OutingViewEntity.class,
                OutingConstants.ETERNAL_OUTING_ID
        );
    }

}
