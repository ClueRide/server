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

/**
 * Defines operations on Location Links.
 */
public interface LocLinkService {

    /**
     * Given a URL passed within a LocLinkEntity instance, create a record in the database for this URL.
     * @param locLinkEntity instance containing the URL.
     * @return Fully-populated LocLink instance with the newly created record ID.
     * @throws java.net.MalformedURLException if the string URL isn't properly formed.
     */
    LocLink createNewLocationLink(LocLinkEntity locLinkEntity) throws MalformedURLException;

    /**
     * Retrieves an existing LocLink if found, or creates a new one if not found.
     *
     * @param locLinkText text of the URL to be obtained.
     * @return Matching LocLink or a new instance if not found.
     */
    LocLink getLocLinkByUrl(String locLinkText) throws MalformedURLException;

}
