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
 * Created by jett on 10/4/17.
 */
package com.clueride.domain.location.loctype;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * JPA implementation of the LocationTypeStore (DAO).
 */
public class LocationTypeStoreJpa implements LocationTypeStore {
    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Override
    public List<LocationType> getLocationTypes() {
        List<LocationType> locationTypes = new ArrayList<>();
        List<LocationTypeBuilder> locTypeBuilders =
                entityManager.createQuery(
                        "SELECT lt from location_type lt"
                ).getResultList();

        for (LocationTypeBuilder locTypeBuilder : locTypeBuilders) {
            locationTypes.add(locTypeBuilder.build());
        }
        return locationTypes;
    }

    @Override
    public LocationType getLocationTypeByName(String locationTypeName) {

        List<LocationTypeBuilder> builders = entityManager.createQuery(
                "SELECT lt FROM location_type lt WHERE lt.name = :locTypeName"
        ).setParameter("locTypeName", locationTypeName)
                .getResultList();

        if (builders.size() == 1) {
            return builders.get(0).build();
        } else {
            throw new RuntimeException("Multiple records found for location type name: " + locationTypeName );
        }
    }

    @Override
    public LocationType getLocationTypeById(Integer locationTypeId) {
        LocationType locationType = entityManager.find(
                LocationTypeBuilder.class,
                locationTypeId
        ).build();
        return locationType;
    }

}
