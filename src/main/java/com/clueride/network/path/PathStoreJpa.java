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
 * Created by jett on 1/20/19.
 */
package com.clueride.network.path;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;

/**
 * JPA implementation of {@link PathStore} that supports GeoJSON query.
 */
public class PathStoreJpa implements PathStore {

    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Override
    public String getPathGeoJson(Integer pathId) {
        return (String) entityManager.createNativeQuery(
        "select cast(row_to_json(fc) as text) " +
                "from (" +
                "select 'FeatureCollection' as type, " +
                "   array_to_json(array_agg(feature)) as features " +
                "from (" +
                "      select" +
                "        'Feature' as type," +
                "        cast(ST_AsGeoJSON(points, 4326) as json) as geometry " +
                "      from path_view" +
                "         where path_id = ?" +
                ") as feature" +
        ") as fc")
                .setParameter(1, pathId)
                .getSingleResult();
    }

    @Override
    public Integer getEdgeCount(Integer pathId) {
        return ((BigInteger) entityManager.createNativeQuery(
                "SELECT count(edge_id) " +
                        "FROM path_view " +
                        " WHERE path_id = ?")
                .setParameter(1, pathId)
                .getSingleResult()
        ).intValue();
    }

    @Override
    public Integer addEdge(Integer pathId) {
        return null;
    }


}
