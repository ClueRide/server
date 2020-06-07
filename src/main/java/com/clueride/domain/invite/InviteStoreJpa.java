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
 * Created by jett on 12/30/18.
 */
package com.clueride.domain.invite;

import org.slf4j.Logger;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;
import java.io.IOException;
import java.util.List;

/**
 * JPA persistence for the Invite entity.
 */

public class InviteStoreJpa implements InviteStore {
    @Inject
    private Logger LOGGER;

    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Override
    public Integer addNew(InviteEntity builder) throws IOException {
        entityManager.persist(builder);
        return builder.getId();
    }

    @Override
    public List<InviteEntity> getInvitationsByOuting(Integer outingId) {
        return null;
    }

    @Override
    public List<InviteEntity> getUpcomingInvitationsByMemberId(Integer memberId) {
        LOGGER.debug("Retrieving invitations for member ID " + memberId);
        List<InviteEntity> inviteEntities;
        // TODO: Ordering should be part of the query
        inviteEntities = entityManager.createQuery(
                "SELECT i FROM InviteEntity i where i.memberId = :memberId AND i.inviteState " +
                        "IN (" +
                        "com.clueride.domain.invite.InviteState.SENT," +
                        "com.clueride.domain.invite.InviteState.ACCEPTED," +
                        "com.clueride.domain.invite.InviteState.DECLINED" +
                        ")",
                InviteEntity.class
        ).setParameter("memberId", memberId)
                .getResultList();
        return inviteEntities;
    }

    @Override
    public InviteEntity getInvitationById(Integer inviteId) {
        return entityManager.find(InviteEntity.class, inviteId);
    }

    @Override
    public InviteEntity save(InviteEntity builder) {
        entityManager.persist(builder);
        return builder;
    }

    @Override
    public InviteEntity accept(Integer inviteId) {
        return updateState(inviteId, InviteState.ACCEPTED);
    }

    @Override
    public InviteEntity decline(Integer inviteId) {
        return updateState(inviteId, InviteState.DECLINED);
    }

    private InviteEntity updateState(Integer inviteId, InviteState state) {
        InviteEntity builder = null;
        try {
            userTransaction.begin();
            builder = getInvitationById(inviteId);
            builder.withState(state);
            entityManager.merge(builder);
            userTransaction.commit();
        } catch (NotSupportedException | SystemException | HeuristicRollbackException | HeuristicMixedException | RollbackException e) {
            e.printStackTrace();
        }
        return builder;
    }

}
