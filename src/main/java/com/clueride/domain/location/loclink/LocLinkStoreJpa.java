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
 * Created by jett on 5/6/19.
 */
package com.clueride.domain.location.loclink;

import java.util.List;

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
 * JPA implementation of {@link LocLinkStore}.
 */
public class LocLinkStoreJpa implements LocLinkStore {

    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Override
    public LocLinkEntity addNew(LocLinkEntity locLinkEntity) {
        try {
            userTransaction.begin();
            entityManager.persist(locLinkEntity);
            userTransaction.commit();
        } catch (RollbackException | NotSupportedException | HeuristicMixedException | HeuristicRollbackException | SystemException e) {
            e.printStackTrace();
        }
        return locLinkEntity;
    }

    @Override
    public LocLinkEntity findByUrl(String locLinkText) {
        List<LocLinkEntity> locLinkEntities =  entityManager.createQuery(
                "SELECT l from LocLinkEntity l " +
                        " WHERE l.link = :linkText"
        )
                .setParameter("linkText", locLinkText)
                /* getSingleResult would throw an exception which is a bother. */
                .getResultList();

        if (locLinkEntities.size() == 0) {
            return null;
        } else {
            return locLinkEntities.get(0);
        }

    }

}
