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

import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public Integer addNew(LocationEntity locationEntity) {
        try {
            userTransaction.begin();
            entityManager.persist(locationEntity);
            userTransaction.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
            e.printStackTrace();
        }
        return locationEntity.getId();
    }

    @Override
    public LocationEntity getLocationBuilderById(Integer id) {
        return entityManager.find(LocationEntity.class, id);
    }

    @Override
    public Collection<LocationEntity> getLocationBuilders() {
        List<LocationEntity> locationList = entityManager.createQuery(
                "SELECT l FROM LocationEntity l",
                LocationEntity.class
        ).getResultList();
        return new ArrayList<>(locationList);
    }

    @Override
    public void update(LocationEntity locationEntity) {
        try {
            userTransaction.begin();
            entityManager.merge(locationEntity);
            userTransaction.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
            LOGGER.error(String.valueOf(e));
            e.printStackTrace();
        }
    }

    @Override
    public void delete(LocationEntity locationEntity) {
        try {
            userTransaction.begin();
            entityManager.remove(
                    entityManager.contains(locationEntity)
                            ? locationEntity
                            : entityManager.merge(locationEntity)
            );
            userTransaction.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
            LOGGER.error(String.valueOf(e));
            e.printStackTrace();
        }
    }

    @Override
    public Iterable<? extends LocationEntity> getThemedLocationBuilders() {
        List<LocationEntity> locationList = entityManager.createQuery(
                "SELECT l FROM LocationEntity l" +
                        " WHERE l.locationTypeId IN (" +
                        "SELECT lt.id from LocationTypeEntity lt WHERE lt.themeId IN (" +
                        "SELECT t.id from ThemeEntity t) )",
                LocationEntity.class
        ).getResultList();
        return new ArrayList<>(locationList);
    }

}
