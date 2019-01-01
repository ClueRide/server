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

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;

/**
 * JPA persistence for the Invite entity.
 */

public class InviteStoreJpa implements InviteStore {
    @Inject
    private Logger LOGGER;

    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Override
    public Integer addNew(Invite.Builder builder) throws IOException {
        entityManager.getTransaction().begin();
        entityManager.persist(builder);
        entityManager.getTransaction().commit();
        return builder.getId();
    }

    @Override
    public List<Invite.Builder> getInvitationsByOuting(Integer outingId) {
        return null;
    }

    @Override
    public List<Invite.Builder> getUpcomingInvitationsByMemberId(Integer memberId) {
        LOGGER.debug("Retrieving invitations for member ID " + memberId);
        List<Invite.Builder> builders;
        // TODO: Ordering and State should be part of the query
        builders = entityManager.createQuery(
                "SELECT i FROM invite i where i.memberId = :memberId AND i.inviteState " +
                        "IN ('SENT', 'ACCEPTED', 'DECLINED')"
        ).setParameter("memberId", memberId)
                .getResultList();
        return builders;
    }

    @Override
    public Invite.Builder getInvitationById(Integer inviteId) {
        entityManager.getTransaction().begin();
        Invite.Builder builder = entityManager.find(Invite.Builder.class, inviteId);
        entityManager.getTransaction().commit();
        return builder;
    }

    @Override
    public Invite.Builder save(Invite.Builder builder) {
        entityManager.getTransaction().begin();
        entityManager.persist(builder);
        entityManager.getTransaction().commit();
        return builder;
    }

}
