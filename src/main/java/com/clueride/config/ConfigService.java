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
 * Created by jett on 8/1/17.
 */
package com.clueride.config;

import java.util.List;
import java.util.Map;

/**
 * Defines interaction with configuration information.
 */
public interface ConfigService {
    /**
     * Returns configured value given the key information.
     * @param key for the configured value.
     * @return String matching the key.
     */
    String get(String key);

    /**
     * List of the valid Issuer Types.
     * @return List of the valid Issuer Types.
     */
    List<String> getAuthIssuerTypes();

    /** Returns list of the URL Strings identifying the Auth Issuers defined. */
    List<String> getAuthIssuers();

    /** Allows local override without placing secret in public code. */
    String getAuthSecret();

    /** Allows local override of a non-expiring Test Token for internal use. */
    String getTestToken();

    /** Account to be used during testing. */
    String getTestAccount();

    /** Provides a Map between tokens and the matching email address which resides in Member table. */
    Map<String, String> getTestAccountMap();

    /** Base URL of the Image server. */
    String getBaseImageUrl();

    /** Base Directory for images on the Image Server. */
    String getImageBaseDirectory();

    /** Account Name for the BadgeOS System account assigned to this Server. */
    String getBadgeOSAccountName();

    /** Password for the BadgeOS account assigned to this server. */
    String getBadgeOSPassword();

    /** URL of the BadgeOS Login page. */
    String getBadgeOSLoginUrlAsString();

}
