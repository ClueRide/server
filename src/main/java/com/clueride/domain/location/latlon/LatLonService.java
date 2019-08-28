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
package com.clueride.domain.location.latlon;

/**
 * Handles LatLonEntity pairs; Nodes which are not necessarily on the Network yet.
 */
public interface LatLonService {
    /**
     * Provide a candidate LatLonEntity and this will persist it to the store.
     * @param latLonEntity Candidate LatLonEntity.
     * @return the newly created LatLonEntity complete with the ID from the DB.
     */
    LatLonEntity addNew(LatLonEntity latLonEntity);

    /**
     * Given a LatLonEntity ID, retrieve the corresponding LatLonEntity.
     * @param id unique ID for the LatLonEntity.
     * @return Location-supporting LatLonEntity.
     */
    LatLonEntity getLatLonById(Integer id);

}
