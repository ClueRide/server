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
package com.clueride.domain.location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.slf4j.Logger;

/**
 * JPA Implementation of the LocationStore (DAO) interface.
 */
public class LocationStoreJpa implements LocationStore {
    @Inject
    private Logger LOGGER;

    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Override
    public Integer addNew(LocationBuilder locationBuilder) throws IOException {
        try {
            userTransaction.begin();
            entityManager.persist(locationBuilder);
            userTransaction.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
            e.printStackTrace();
        }
        return locationBuilder.getId();
    }

    @Override
    public LocationBuilder getLocationBuilderById(Integer id) {
        return entityManager.find(LocationBuilder.class, id);
    }

    @Override
    public Collection<LocationBuilder> getLocationBuilders() {
        Collection<LocationBuilder> builderCollection = new ArrayList<>();
        List<LocationBuilder> locationList = entityManager.createQuery(
                "SELECT l FROM location l"
        ).getResultList();
        builderCollection.addAll(locationList);
        return builderCollection;
    }

    @Override
    public void update(LocationBuilder locationBuilder) {
        try {
            userTransaction.begin();
            entityManager.merge(locationBuilder);
            userTransaction.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
            LOGGER.error(String.valueOf(e));
            e.printStackTrace();
        }
    }

    @Override
    public void delete(LocationBuilder locationBuilder) {
        try {
            userTransaction.begin();
            entityManager.remove(
                    entityManager.contains(locationBuilder)
                            ? locationBuilder
                            : entityManager.merge(locationBuilder)
            );
            userTransaction.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
            LOGGER.error(String.valueOf(e));
            e.printStackTrace();
        }
    }

}
