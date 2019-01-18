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
 * Created by jett on 8/13/17.
 */
package com.clueride.domain.account.member;

import java.util.List;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * JPA Implementation of the MemberStore (DAO) interface.
 */
@ApplicationScoped
public class MemberStoreJpa implements MemberStore {
    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Override
    public MemberBuilder addNew(MemberBuilder memberBuilder) {
        // TODO: CA-405/CA-409
        try {
            userTransaction.begin();
            entityManager.persist(memberBuilder);
            userTransaction.commit();
        } catch (NotSupportedException | SystemException | HeuristicMixedException | RollbackException | HeuristicRollbackException e) {
            e.printStackTrace();
        }
        return memberBuilder;
    }

    @Override
    public MemberBuilder getMemberById(Integer id) {
        return entityManager.find(MemberBuilder.class, id);
    }

    @Override
    public List<MemberBuilder> getMemberByName(String name) {
        return null;
    }

    @Override
    public MemberBuilder getMemberByEmail(InternetAddress emailAddress) {
        MemberBuilder memberBuilder;
            memberBuilder = entityManager
                    .createQuery(
                            "select m from member m where m.emailAddress = :emailAddress",
                            MemberBuilder.class
                    )
                    .setParameter("emailAddress", emailAddress.toString())
                    .getSingleResult();

        return memberBuilder;
    }

    @Override
    public void update(Member member) {

    }

    @Override
    public List<MemberBuilder> getAllMembers() {
        return entityManager.createQuery("SELECT m FROM member m").getResultList();
    }

}
