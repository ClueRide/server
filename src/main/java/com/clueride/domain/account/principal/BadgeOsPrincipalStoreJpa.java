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
 * Created by jett on 8/28/18.
 */
package com.clueride.domain.account.principal;

import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static java.util.Objects.requireNonNull;

/**
 * JPA implementation of the Badge OS Principal Store.
 */
public class BadgeOsPrincipalStoreJpa implements BadgeOsPrincipalStore {
    @PersistenceContext(unitName = "badgeOS")
    private EntityManager entityManager;

    @Override
    public BadgeOsPrincipalBuilder getBadgeOsPrincipalForEmailAddress(InternetAddress emailAddress) {
        requireNonNull(emailAddress);

        BadgeOsPrincipalBuilder principalBuilder;
        try {
            principalBuilder = entityManager
                    .createQuery(
                            "SELECT p from badgeos_principal p where p.emailAddressString = :emailAddress",
                            BadgeOsPrincipalBuilder.class
                    )
                    .setParameter("emailAddress", emailAddress.toString())
                    .getSingleResult();
        } catch (Exception e) {
            throw e;
        }

        return principalBuilder;
    }

    @Override
    public List<BadgeOsPrincipalBuilder> getAll() {
        return entityManager.createQuery("SELECT p FROM badgeos_principal p").getResultList();
    }

}
