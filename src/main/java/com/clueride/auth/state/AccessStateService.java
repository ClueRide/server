/*
 * Copyright 2018 Jett Marks
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
 * Created by jett on 12/10/18.
 */
package com.clueride.auth.state;

/**
 * Supports AccessStateWebService REST API.
 */
public interface AccessStateService {
    /**
     * Performs a series of tests to assure that we recognize the device from which
     * the request is being made.
     * @param authHeader String, which could be empty or null, that is expected to represent
     *                   the <em>Authorization</em> header for any request to the REST API.
     * @return true if the device has been registered and the token may be used to conduct other
     * calls to the REST API. If false, it could indicate a few things (each of which are logged):
     * <ul>
     *     <li>Is the header present and non-empty?</li>
     *     <li>Does the value of the header contain the 'Bearer' string?</li>
     *     <li>If it is a Test Token, does the matching account exist within the system?</li>
     *     <li>If not a Test Token, does the 3rd-party Identity Provider recognize the token?</li>
     *     <li>If no one recognizes the token, throw an exception?</li>
     * </ul>
     */
    Boolean isRegistered(String authHeader);

}
