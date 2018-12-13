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

import javax.inject.Inject;
import javax.mail.internet.InternetAddress;

import org.slf4j.Logger;

import com.clueride.RecordNotFoundException;
import com.clueride.auth.access.AccessTokenService;
import com.clueride.auth.identity.ClueRideIdentity;
import com.clueride.config.ConfigService;
import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.member.MemberService;
import com.clueride.domain.account.principal.BadgeOsPrincipal;
import com.clueride.domain.account.principal.BadgeOsPrincipalService;

/**
 * Implementation of {@link AccessStateService}.
 */
public class AccessStateServiceImpl implements AccessStateService {
    @Inject
    private Logger LOGGER;

    private final AccessTokenService accessTokenService;
    private final ConfigService configService;
    private final BadgeOsPrincipalService badgeOsPrincipalService;
    private final MemberService memberService;

    @Inject
    public AccessStateServiceImpl(
            AccessTokenService accessTokenService,
            ConfigService configService,
            BadgeOsPrincipalService badgeOsPrincipalService,
            MemberService memberService
    ) {
        this.accessTokenService = accessTokenService;
        this.configService = configService;
        this.badgeOsPrincipalService = badgeOsPrincipalService;
        this.memberService = memberService;
    }

    @Override
    public Boolean isRegistered(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        String token = authHeader.substring("Bearer".length()).trim();
        if (accessTokenService.isSessionActive(token)) {
            return true;
        } else {
            if (token.equals(configService.getTestToken())) {
                LOGGER.info("Allowing Test Token");
                return true;
            }
            try {
                ClueRideIdentity clueRideIdentity = accessTokenService.getIdentity(token);
                createOrUpdatePrincipal(clueRideIdentity);
            } catch (RecordNotFoundException rnfe) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks for existence of Member and BadgeOsPrincipal and updates Member record if not present.
     *
     * At this time, we're only registering principals which are found in Badge OS, so an exception
     * is thrown if no Badge OS principal matches the ClueRideIdentity account.
     * @param clueRideIdentity from the Identity Provider.
     */
    private void createOrUpdatePrincipal(ClueRideIdentity clueRideIdentity) {

        /* Find Badge OS record. */
        InternetAddress emailAddress = clueRideIdentity.getEmail();
        BadgeOsPrincipal badgeOsPrincipal = null;
        try {
            badgeOsPrincipal = badgeOsPrincipalService.getBadgeOsPrincipal(emailAddress);
        } catch (RecordNotFoundException rnfe) {
            /* Handle the same as null returned below. */
        }

        if (badgeOsPrincipal == null) {
            LOGGER.warn("Unable to find {} in Badge OS", emailAddress.toString());
            throw(new RecordNotFoundException());
        }

        // TODO: CA-405 connect new identities with updated records & test coverage.
        /* Check to see if we have a Member Record. */
        Member member = null;
        try {
            member = memberService.getMemberByEmail(emailAddress);
        } catch (RecordNotFoundException rnfe) {
            /* No existing Member record, let's create one from ClueRideIdentity. */
            memberService.createNewMember(clueRideIdentity);
        }

    }
}
