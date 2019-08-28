/**
 * Copyright 2015 Jett Marks
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Created by jett on 11/23/15.
 */
package com.clueride.domain.location;

import java.io.IOException;
import java.util.Collection;

public interface LocationStore {

    /**
     * Accepts a partially constructed Location to the store and returns the ID.
     * Implementations are expected to write to permanent storage.
     * @param locationEntity newly proposed Location, ready to persist.
     * @return ID of the new Location.
     */
    Integer addNew(LocationEntity locationEntity) throws IOException;

    /**
     * Returns the Location Builder matching the unique ID.
     * @param id unique identifier for the Location Builder.
     * @return fully-populated Location Builder.
     */
    LocationEntity getLocationBuilderById(Integer id);

    /**
     * Returns the list of Location Builders maintained by this store.
     * @return Collection of all Locations in the store.
     */
    Collection<LocationEntity> getLocationBuilders();

    /**
     * Accepts an existing Location and updates the persistent record with new information.
     * @param locationEntity to be updated.
     */
    void update(LocationEntity locationEntity);

    /**
     * Given the Location record, remove it from storage.
     * @param location Instance to be deleted.
     */
    void delete(LocationEntity location);

    /**
     * Retrieves list of Themed Location Builders.
     * @return Iterable over themed LocationBuilders
     */
    Iterable<? extends LocationEntity> getThemedLocationBuilders();

}
