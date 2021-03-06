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
 * Given an Email Address, provides the matching principal from BadgeOS.
 */
public interface BadgeOsPrincipalService {
    /**
     * Given an Email Address, provides the matching principal from BadgeOS.
     * @param emailAddress to be matched within BadgeOS.
     * @return Fully-populated BadgeOsPrincipal.
     */
    BadgeOsPrincipal getBadgeOsPrincipal(InternetAddress emailAddress);

    BadgeOsPrincipal getBadgeOsPrincipal(String emailAddress);

    List<BadgeOsPrincipal> getFilteredPrincipals(BadgeOsPrincipalFilter filter);

    /**
     * Retrieves the existing principal for this session.
     * @return BadgeOsPrincipal for the current validated (@Secured) session.
     */
    BadgeOsPrincipal getCurrentSessionPrincipal();

}
