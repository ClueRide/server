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
 * Created by jett on 1/1/19.
 */
package com.clueride.domain.outing;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static java.util.Objects.requireNonNull;

/**
 * JPA-based implementation of OutingStore.
 */
public class OutingStoreJpa implements OutingStore {
    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Override
    public Integer addNew(OutingViewBuilder builder) throws IOException {
        return null;
    }

    @Override
    public OutingViewBuilder getOutingById(Integer outingId) {
        requireNonNull(outingId, "Must specify an outing ID");
        return entityManager.find(OutingViewBuilder.class, outingId);
    }

    @Override
    public OutingViewBuilder getOutingViewById(Integer outingId) {
        requireNonNull(outingId, "Must specify an outing ID");
        return entityManager.find(OutingViewBuilder.class, outingId);
    }

}
