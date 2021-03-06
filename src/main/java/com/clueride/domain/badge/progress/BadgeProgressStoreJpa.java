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
 * Created by jett on 6/15/19.
 */
package com.clueride.domain.badge.progress;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;

/**
 * JPA implementation of {@link BadgeProgressStore}.
 */
public class BadgeProgressStoreJpa implements BadgeProgressStore {
    @Inject
    private Logger LOGGER;

    @PersistenceContext(unitName = "badgeOS")
    private EntityManager entityManager;

    @Override
    public BadgeProgressEntity getBadgeProgressById(int badgeId) {
        return entityManager.find(BadgeProgressEntity.class, badgeId);
    }

}
