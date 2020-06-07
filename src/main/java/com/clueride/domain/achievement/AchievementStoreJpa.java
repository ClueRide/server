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
 * Created by jett on 8/20/19.
 */
package com.clueride.domain.achievement;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * JPA-based implementation of {@link AchievementStore}.
 */
public class AchievementStoreJpa implements AchievementStore {
    @Inject
    private Logger LOGGER;

    @PersistenceContext(unitName = "badgeOS")
    private EntityManager entityManager;

    @Override
    public List<AchievementEntity> getAllAchievements() {
        return entityManager.createQuery(
                "SELECT ach FROM AchievementEntity ach",
                AchievementEntity.class
        ).getResultList();
    }

    @Override
    public List<AchievementEntity> getAchievementsForUser(Integer userId) {
        return entityManager.createQuery("SELECT ach FROM AchievementEntity ach " +
                "WHERE ach.userId = :userId",
                AchievementEntity.class
        ).setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public void removeAchievement(Integer achievementId) {
        LOGGER.warn("Not yet implemented");
    }

}
