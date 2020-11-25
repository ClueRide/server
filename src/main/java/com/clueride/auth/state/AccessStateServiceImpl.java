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

import com.clueride.RecordNotFoundException;
import com.clueride.auth.InvalidAuthTokenException;
import com.clueride.auth.access.AccessTokenService;
import com.clueride.auth.identity.ClueRideIdentity;
import com.clueride.auth.session.ClueRideSessionDto;
import com.clueride.auth.session.ClueRideSessionService;
import com.clueride.config.ConfigService;
import com.clueride.domain.account.badgeos.BadgeOsUserEntity;
import com.clueride.domain.account.badgeos.BadgeOsUserService;
import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.member.MemberEntity;
import com.clueride.domain.account.member.MemberService;
import com.clueride.domain.account.principal.BadgeOsPrincipal;
import com.clueride.domain.account.principal.BadgeOsPrincipalService;
import com.clueride.domain.invite.InviteService;
import com.clueride.domain.outing.OutingConstants;
import com.clueride.domain.outing.OutingService;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;

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
    private final OutingService outingService;
    private final ClueRideSessionService clueRideSessionService;
    private final InviteService inviteService;
    private final BadgeOsUserService badgeOsUserService;

    @Inject
    public AccessStateServiceImpl(
            AccessTokenService accessTokenService,
            ConfigService configService,
            BadgeOsPrincipalService badgeOsPrincipalService,
            MemberService memberService,
            OutingService outingService,
            ClueRideSessionService clueRideSessionService,
            InviteService inviteService,
            BadgeOsUserService badgeOsUserService
    ) {
        this.accessTokenService = accessTokenService;
        this.configService = configService;
        this.badgeOsPrincipalService = badgeOsPrincipalService;
        this.memberService = memberService;
        this.outingService = outingService;
        this.clueRideSessionService = clueRideSessionService;
        this.inviteService = inviteService;
        this.badgeOsUserService = badgeOsUserService;
    }

    @Override
    public Boolean isRegistered(String authHeader) {

        try {
            accessTokenService.validateAuthHeader(authHeader);
        } catch (InvalidAuthTokenException e) {
            /* No need to reveal too much info regarding auth failure; leave it in the logs. */
            LOGGER.error("isRegistered() finds problem with authHeader", e);
            return false;
        }

        String token = authHeader.substring("Bearer".length()).trim();
        if (accessTokenService.isSessionActive(token)) {
            return true;
        } else {
            if (isTestAccount(token)) {
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

    /* Experimental. */
    @Override
    public AccountState getAccountState(String authHeader) {
        try {
            accessTokenService.validateAuthHeader(authHeader);
        } catch (InvalidAuthTokenException e) {
            LOGGER.error("getAccountState() finds problem with authHeader", e);
            return AccountState.INVALID;
        }

        String token = authHeader.substring("Bearer".length()).trim();
        if (accessTokenService.isSessionActive(token)) {
            return AccountState.EXISTING;
        }

        if (isTestAccount(token)) {
            return AccountState.TEST;
        }

        try {
            ClueRideIdentity clueRideIdentity = accessTokenService.getIdentity(token);
            createOrUpdatePrincipal(clueRideIdentity);
        } catch (RecordNotFoundException rnfe) {
            return AccountState.UNRECOGNIZED;
        }

        return AccountState.NEW;
    }

    @Override
    public Member registerPossibleNewMember(Member member, String authHeader) {
        /* Throws exception if programmer error messes up supplying a valid token. */
        accessTokenService.validateAuthHeader(authHeader);
        String token = authHeader.substring("Bearer".length()).trim();

        if (member.getEmailAddress().isEmpty()) {
            throw new RuntimeException("Missing Email Address on Member instance");
        }

        /* Ask 3rd-party Auth service who it thinks is registering with this token. */
        ClueRideIdentity clueRideIdentity = accessTokenService.getIdentity(token);

        MemberEntity memberEntity = MemberEntity.from(member);
        /* Verify that the submitted member's email address matches the Auth service's idea of that email address. */
        if (member.getEmailAddress().equals(clueRideIdentity.getEmail().getAddress())) {
            LOGGER.info("Registering {}", member.getEmailAddress());

            /* Check to see if records already exist for this email address. */
            Member existingMember = null;
            try {
                existingMember = memberService.getMemberByEmail(member.getEmailAddress());
            } catch (AddressException e) {
                e.printStackTrace();
            }

            /* Establish new Member record. */
            if (existingMember == null) {
                memberEntity = memberService.createNewMember(clueRideIdentity);
            } else {
                memberEntity = MemberEntity.from(existingMember);
            }

            ClueRideSessionDto clueRideSessionDto = new ClueRideSessionDto();

            /* Check to see if existing BadgeOS record exists. */
            BadgeOsUserEntity existingBadgeOsUserEntity = badgeOsUserService.getByEmailAddress(member.getEmailAddress());

            /* Establish BadgeOS record. */
            BadgeOsUserEntity badgeOsUserEntity;
            if (existingBadgeOsUserEntity == null) {
                badgeOsUserEntity = BadgeOsUserEntity.from(memberEntity, clueRideIdentity);
                badgeOsUserService.add(badgeOsUserEntity);
            } else {
                badgeOsUserEntity = existingBadgeOsUserEntity;
            }

            clueRideSessionDto.setBadgeOSPrincipal(
                    badgeOsPrincipalService.getBadgeOsPrincipal(memberEntity.getEmailAddress())
            );
            memberEntity.withBadgeOSId(badgeOsUserEntity.getId());

            /* Invite them to the "eternal" Outing. */
            addEternalInvite(clueRideIdentity, memberEntity, clueRideSessionDto);

            clueRideSessionService.setSessionForToken(token, clueRideSessionDto);

            /* This will not work even if we have the DTO placed inside the session. */
            // TODO: SVR-113: How to register outside of @Secured.
//            registerService.register();
        }

        return memberEntity.build();
    }

    private void addEternalInvite(ClueRideIdentity clueRideIdentity, MemberEntity memberEntity, ClueRideSessionDto clueRideSessionDto) {
        try {
            clueRideSessionDto.setInvite(
                    inviteService.createNew(OutingConstants.ETERNAL_OUTING_ID, memberEntity.getId())
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        clueRideSessionDto.setMember(memberEntity.build());
        clueRideSessionDto.setClueRideIdentity(clueRideIdentity);
        clueRideSessionDto.setOutingView(
                outingService.getViewById(OutingConstants.ETERNAL_OUTING_ID)
        );
    }

    /**
     * Returns true if the given token represents a TEST account.
     *
     * Token is assumed to have been validated.
     *
     * @param token String representing a Test account.
     * @return true if this token represents a TEST account.
     */
    private boolean isTestAccount(String token) {
        if (token.equals(configService.getTestToken())) {
            LOGGER.info("Allowing Test Token");
            return true;
        }
        if (configService.getTestAccountMap().containsKey(token)) {
            LOGGER.info("Allowing Configurable Test Token");
            return true;
        }
        return false;
    }

    /**
     * Checks for existence of Member and BadgeOsPrincipal and updates Member record if not present.
     *
     * At this time, we're only registering principals which are found in Badge OS, so an exception
     * is thrown if no Badge OS principal matches the ClueRideIdentity account.
     * @param clueRideIdentity from the Identity Provider.
     */
    private void createOrUpdatePrincipal(ClueRideIdentity clueRideIdentity) {
        // TODO: Can serve as SVR-111 and/or SVR-112 code

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

        // TODO: CA-409 connect new identities with updated records & test coverage.
        /* Check to see if we have a Member Record. */
        try {
            memberService.getMemberByEmail(emailAddress);
        } catch (RecordNotFoundException rnfe) {
            /* No existing Member record, let's create one from ClueRideIdentity. */
            memberService.createNewMember(clueRideIdentity);
        }
    }

}
