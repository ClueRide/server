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
 * Created by jett on 6/19/19.
 */
package com.clueride.domain.step.parent;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;

/**
 * JPA implementation of {@link StepParentStore}.
 */
public class StepParentStoreJpa implements StepParentStore {
    @Inject
    private Logger LOGGER;

    @PersistenceContext(unitName = "badgeOS")
    private EntityManager entityManager;

    @Override
    public List<StepParentEntity> getAllStepParents() {
        LOGGER.debug("Retrieving all Step Parents");

        return entityManager.createQuery(
                "SELECT s FROM StepParentEntity s"
        ).getResultList();
    }

}
