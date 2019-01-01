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
 * Created by jett on 12/1/18.
 */
package com.clueride.auth;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clueride.RecordNotFoundException;
import com.clueride.auth.access.AccessTokenService;
import com.clueride.auth.identity.ClueRideIdentity;
import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.member.MemberService;
import com.clueride.domain.account.principal.BadgeOsPrincipalService;

/**
 * Responds to the Event that requests placing Principal instances into user's session.
 */
@RequestScoped
public class ClueRideSessionProducer implements Serializable {
    @Inject
    private Logger LOGGER;

    @Inject
    private AccessTokenService accessTokenService;

    @Inject
    private BadgeOsPrincipalService badgeOsPrincipalService;

    @Inject
    private MemberService memberService;

    private String accessToken;

//    @Inject
//    public ClueRideSessionProducer(
//            AccessTokenService accessTokenService,
//            BadgeOsPrincipalService badgeOsPrincipalService
//    ) {
//        this.accessTokenService = accessTokenService;
//        this.badgeOsPrincipalService = badgeOsPrincipalService;
//    }

    /* Records the key info each time a request is made. */
    public void handleUserRegisteredEvent(@Observes @ClueRideSession String accessToken) {
        this.accessToken = accessToken;
    }

    @Produces
    @SessionScoped
    @ClueRideSession
    /* Invoked once per session. */
    private ClueRideSessionDto produceClueRideSessionDto() {
        ClueRideSessionDto clueRideSessionDto = new ClueRideSessionDto();

        ClueRideIdentity clueRideIdentity = accessTokenService.getIdentity(accessToken);
        clueRideSessionDto.setClueRideIdentity(clueRideIdentity);

        String emailAddressString = clueRideIdentity.getEmail().toString();
        clueRideSessionDto.setBadgeOSPrincipal(badgeOsPrincipalService.getBadgeOsPrincipal(emailAddressString));

        try {
            Member member = memberService.getMemberByEmail(clueRideIdentity.getEmail());
            clueRideSessionDto.setMember(member);
        } catch (RecordNotFoundException rnfe) {
            // TODO: CA-409
            LOGGER.error("Unable to find member record for email: {}", emailAddressString);
        }

        return clueRideSessionDto;
    }

}
