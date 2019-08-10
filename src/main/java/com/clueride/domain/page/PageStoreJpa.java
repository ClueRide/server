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
 * Created by jett on 8/10/19.
 */
package com.clueride.domain.page;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * JPA-based implementation of {@link PageStore}.
 */
public class PageStoreJpa implements PageStore {
    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Override
    public PageEntity getPageBySlug(String slug) {

        return entityManager.createQuery(
                "SELECT p from Page p " +
                        "WHERE p.pageSlug = :slug",
                    PageEntity.class
                )
                .setParameter("slug", slug)
                .getSingleResult();

    }

}
