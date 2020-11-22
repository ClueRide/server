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
 * Created by jett on 7/29/19.
 */
package com.clueride.domain.achievement.map;

import com.clueride.util.MainLogger;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * JPA implementation of {@link ArrivalStepStore}.
 */
public class ArrivalStepStoreJpa implements ArrivalStepStore {

    @Inject
    @MainLogger
    private Logger LOGGER;

    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Override
    public List<ArrivalStepEntity> getAllRecords() {
        LOGGER.debug("Retrieving all Steps");
        List<ArrivalStepEntity> entities;
        entities = entityManager.createQuery(
                "SELECT s FROM ArrivalStepEntity s",
                ArrivalStepEntity.class
        ).getResultList();
        return entities;
    }

}
