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
 * Created by jett on 5/6/19.
 */
package com.clueride.domain.location.loclink;

/**
 * Defines the persistence layer for Location Links.
 */
public interface LocLinkStore {

    /**
     * Given the data within the Location Link Entity, create a new record
     * and return the populated {@link LocLinkEntity} from the new record including ID.
     * @param locLinkEntity holds the validated URL string.
     * @return instance that also carries the unique record ID.
     */
    LocLinkEntity addNew(LocLinkEntity locLinkEntity);

    /**
     * Retrieves matching Location Link record if it exists and returns null otherwise.
     * @param locLinkText URL text to match exactly.
     * @return matching {@link LocLinkEntity} record if it exists and null otherwise.
     */
    LocLinkEntity findByUrl(String locLinkText);

}
