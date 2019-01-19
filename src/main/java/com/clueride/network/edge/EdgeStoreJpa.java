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
 * Created by jett on 1/13/19.
 */
package com.clueride.network.edge;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * JPA-based implementation of {@link EdgeStore}.
 */
public class EdgeStoreJpa implements EdgeStore {

    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Override
    public EdgeBuilder getEdgeById(Integer id) {
        return entityManager.find(EdgeBuilder.class, id);
    }

    @Override
    public String getEdgeGeoJson(Integer edgeId) {
        return (String) entityManager.createNativeQuery("select cast(row_to_json(feature) as text) " +
                "from (" +
                "      select" +
                "        'Feature' as type," +
                "        cast(ST_AsGeoJSON(points, 4326) as text) as geometry," +
                "        (" +
                "          select json_strip_nulls(row_to_json(p))" +
                "          from (" +
                "                 select" +
                "                   id," +
                "                   name," +
                "                   track_reference" +
                "               ) p" +
                "        ) as properties" +
                "      from edge" +
                "         where id = ?" +
                ") as feature")
                    .setParameter(1, edgeId)
                .getSingleResult();
    }

}
