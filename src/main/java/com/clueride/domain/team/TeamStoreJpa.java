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
 * Created by jett on 10/31/18.
 */
package com.clueride.domain.team;

import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;
import java.util.List;

/**
 * JPA implementation of {@link TeamStore}.
 */
public class TeamStoreJpa implements TeamStore {
    @Inject
    private Logger LOGGER;

    @Resource
    private UserTransaction userTransaction;

    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Override
    public TeamEntity addNew(TeamEntity teamEntity) {
        LOGGER.info("Creating a new Team: " + teamEntity.getName());
        try {
            userTransaction.begin();
            entityManager.persist(teamEntity);
            userTransaction.commit();
        } catch (NotSupportedException
                | RollbackException
                | HeuristicMixedException
                | HeuristicRollbackException
                | SystemException e
        ) {
            e.printStackTrace();
        }
        return teamEntity;
    }

    @Override
    public List<TeamEntity> getTeams() {
        LOGGER.info("Retrieving full list of Teams");
        return (List<TeamEntity>) entityManager.createQuery(
                "SELECT t FROM TeamEntity t " +
                        "left outer join OutingViewEntity o " +
                        "on o.teamId = t.id " +
                        "order by o.courseId, " +
                        "o.scheduledTime desc",
                TeamEntity.class
        ).getResultList();
    }

    @Override
    public TeamEntity getTeamById(Integer teamId) {
        LOGGER.info("Retrieving Team by ID: " + teamId);
        return entityManager.find(TeamEntity.class, teamId);
    }

    @Override
    public TeamEntity updateTeam(TeamEntity teamEntity) {
        LOGGER.info("Updating an existing Team: " + teamEntity.getId());
        entityManager.merge(teamEntity);
        return teamEntity;
    }

}
