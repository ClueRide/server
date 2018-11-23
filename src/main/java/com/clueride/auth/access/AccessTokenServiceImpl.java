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
 * Created by jett on 11/21/18.
 */
package com.clueride.auth.access;

import javax.inject.Inject;

import org.slf4j.Logger;

/**
 * TODO: Temporary implementation for proof-of-concept.
 */
public class AccessTokenServiceImpl implements AccessTokenService {

    @Inject
    private Logger LOGGER;

    @Override
    public String getPrincipalString(String token) {
        LOGGER.debug("Looking up Principal by Access Token");
        return "Placeholder";
    }

    @Override
    public void emptyCache() {
        LOGGER.debug("Clearing the Access Token Cache");
    }

}
