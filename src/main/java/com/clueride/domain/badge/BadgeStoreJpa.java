/*
 * Copyright 2018 Jett Marks
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
 * Created by jett on 8/26/18.
 */
package com.clueride.domain.badge;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * JPA implementation of Badge Store.
 */
public class BadgeStoreJpa implements BadgeStore {
    @Inject
    private Logger LOGGER;

    @PersistenceContext(unitName = "badgeOS")
    private EntityManager entityManager;

    @Override
    // TODO: This won't work - no "DB" joins retrieve the records by User.
    public List<BadgeEntity> getAwardedBadgesForUser(Integer userId) {
        LOGGER.debug("Retrieving Badges for User ID {}", userId);
        List<BadgeEntity> builderList;
        builderList = entityManager.createQuery(
                "SELECT b FROM BadgeEntity b WHERE b.userId = :userId",
                BadgeEntity.class
        )
                .setParameter("userId", userId)
                .getResultList();
        return builderList;
    }

}
