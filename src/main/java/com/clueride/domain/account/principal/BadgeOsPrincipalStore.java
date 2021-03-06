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
 * Created by jett on 8/28/18.
 */
package com.clueride.domain.account.principal;

import java.util.List;

import javax.mail.internet.InternetAddress;

/**
 * How we retrieve BadgeOS Principals from the BadgeOS system.
 */
public interface BadgeOsPrincipalStore {

    /**
     * Retrieves the BadgeOsPrincipal for the given email address.
     * @param emailAddress supplied by the user's credentials.
     * @return Principal containing the User ID, Display Name, and the email address.
     */
    BadgeOsPrincipalEntity getBadgeOsPrincipalForEmailAddress(InternetAddress emailAddress);

    /**
     * Retrieves the full set of Principals.
     *
     * @return List of all {@link BadgeOsPrincipal}.
     */
    List<BadgeOsPrincipalEntity> getAll();

}
