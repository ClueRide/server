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
 * Created by jett on 9/10/17.
 */
package com.clueride.domain.location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * JPA Implementation of the LocationStore (DAO) interface.
 */
public class LocationStoreJpa implements LocationStore {

    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    /**
     * Likely to be removed.
     * @param location newly and fully constructed Location, ready to persist.
     * @return
     * @throws IOException
     */
    @Override
    public Integer addNew(Location location) throws IOException {
        return null;
    }

    @Override
    public Integer addNew(LocationBuilder locationBuilder) throws IOException {
        entityManager.persist(locationBuilder);
        return locationBuilder.getId();
    }

    @Override
    public Location getLocationById(Integer id) {
        return null;
    }

    @Override
    public LocationBuilder getLocationBuilderById(Integer id) {
        LocationBuilder locationBuilder = entityManager.find(LocationBuilder.class, id);
        return locationBuilder;
    }

    @Override
    public Collection<Location> getLocations() {
        Collection<Location> locations = new ArrayList<>();
        Collection<LocationBuilder> builderCollection = getLocationBuilders();
        for (LocationBuilder builder : builderCollection) {
            try {
                locations.add(builder.build());
            } catch (IllegalStateException ise) {
                /* Defer to returning Builders; deprecated for now. */
            }
        }
        return locations;
    }

    @Override
    public Collection<LocationBuilder> getLocationBuilders() {
        Collection<LocationBuilder> builderCollection = new ArrayList<>();
        List<LocationBuilder> locationList = entityManager.createQuery(
                "SELECT l FROM location l"
        ).getResultList();
        builderCollection.addAll(locationList);
        return builderCollection;
    }

    /**
     * @deprecated - use method accepting Location.Builder.
     * @param location to be updated.
     */
    @Override
    public void update(Location location) {

    }

    @Override
    public void update(LocationBuilder locationBuilder) {
        entityManager.persist(locationBuilder);
    }
}
