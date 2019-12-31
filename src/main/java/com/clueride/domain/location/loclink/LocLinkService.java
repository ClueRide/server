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

import java.net.MalformedURLException;

import com.clueride.domain.location.LocationEntity;

/**
 * Defines operations on Location Links.
 */
public interface LocLinkService {

    /**
     * Given a URL passed within a LocLinkEntity instance, create a record in the database for this URL.
     * @param locLinkEntity instance containing the URL.
     * @return Fully-populated LocLink instance with the newly created record ID.
     * @throws MalformedURLException if the string URL isn't properly formed.
     */
    LocLinkEntity createNewLocationLink(LocLinkEntity locLinkEntity) throws MalformedURLException;

    /**
     * Retrieves an existing LocLink if found, or creates a new one if not found.
     *
     * @param locLinkText text of the URL to be obtained.
     * @return Matching LocLink or a new instance if not found.
     * @throws MalformedURLException if the string URL isn't properly formed.
     */
    LocLinkEntity getLocLinkByUrl(String locLinkText) throws MalformedURLException;

    /**
     * Performs checks and validates the link text before creating an appropriate
     * instance of {@link LocLinkEntity} for adding to a {@link LocationEntity}.
     * @param locLinkEntity instance to be checked; supplies the link text.
     * @return Either null, or a persisted instance of a Link.
     * @throws MalformedURLException if the string URL isn't properly formed.
     */
    LocLinkEntity validateAndPrepareFromUserInput(LocLinkEntity locLinkEntity) throws MalformedURLException;
}
