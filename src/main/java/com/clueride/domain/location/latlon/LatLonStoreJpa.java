/*
 * Copyright 2017 Jett Marks
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
 * Created by jett on 9/10/17.
 */
package com.clueride.domain.location.latlon;

import java.io.IOException;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * JPA implementation of the Loc LatLonEntity Store.
 */
public class LatLonStoreJpa implements LatLonStore {
    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Override
    public Integer addNew(LatLonEntity latLonEntity) throws IOException {
        try {
            userTransaction.begin();
            entityManager.persist(latLonEntity);
            userTransaction.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
            e.printStackTrace();
        }
        return latLonEntity.getId();
    }

    @Override
    public LatLonEntity getLatLonById(Integer id) {
        LatLonEntity latLonEntity = entityManager.find(LatLonEntity.class, id);
        return latLonEntity;
    }

}
