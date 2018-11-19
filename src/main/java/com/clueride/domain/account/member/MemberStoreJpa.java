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

import java.io.IOException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;

/**
 * JPA Implementation of the MemberStore (DAO) interface.
 */
@ApplicationScoped
public class MemberStoreJpa implements MemberStore {
    @Inject
    private EntityManager entityManager;

    @Override
    public Integer addNew(Member member) throws IOException {
        entityManager.persist(Member.Builder.from(member));
        return member.getId();
    }

    @Override
    public Member.Builder getMemberById(Integer id) {
        return entityManager.find(Member.Builder.class, id);
    }

    @Override
    public List<Member.Builder> getMemberByName(String name) {
        return null;
    }

    @Override
    public Member.Builder getMemberByEmail(InternetAddress emailAddress) {
        Member.Builder memberBuilder;
            memberBuilder = entityManager
                    .createQuery(
                            "select m from member m where m.emailAddress = :emailAddress",
                            Member.Builder.class
                    )
                    .setParameter("emailAddress", emailAddress.toString())
                    .getSingleResult();

        return memberBuilder;
    }

    @Override
    public void update(Member member) {

    }

    @Override
    public List<Member.Builder> getAllMembers() {
        return entityManager.createQuery("SELECT m FROM member m").getResultList();
    }

}
