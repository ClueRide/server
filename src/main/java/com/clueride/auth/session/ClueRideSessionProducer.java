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
package com.clueride.auth.session;

import java.io.Serializable;
import java.util.List;

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
import com.clueride.domain.invite.Invite;
import com.clueride.domain.invite.InviteService;
import com.clueride.domain.outing.OutingService;
import com.clueride.domain.outing.OutingView;
import static java.util.Objects.requireNonNull;

/**
 * Responds to the Event that requests placing Principal instances into user's session.
 *
 * Design on the Wiki: http://bikehighways.wikidot.com/principal-into-session.
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
    private ClueRideSessionService clueRideSessionService;

    @Inject
    private InviteService inviteService;

    @Inject
    private MemberService memberService;

    @Inject
    private OutingService outingService;


    private String accessToken;

    /* Records the key info each time a request is made. */
    public void handleUserRegisteredEvent(@Observes @ClueRideSession String accessToken) {
        this.accessToken = accessToken;
    }

    @Produces
    @SessionScoped
    @ClueRideSession
    /* Invoked once per session, but maintaining session is tricky across platforms. */
    private ClueRideSessionDto produceClueRideSessionDto() {
        requireNonNull(accessToken, "Empty Token. Is this endpoint behind the @Secured annotation?");
        ClueRideSessionDto clueRideSessionDto = clueRideSessionService.getSessionFromToken(accessToken);

        /* Check if we already have a session object for this token ... */
        if (clueRideSessionDto != null) {
            return clueRideSessionDto;
        }

        /* ... if not, create a new one. */
        clueRideSessionDto = new ClueRideSessionDto();

        ClueRideIdentity clueRideIdentity = accessTokenService.getIdentity(accessToken);
        clueRideSessionDto.setClueRideIdentity(clueRideIdentity);

        String emailAddressString = addBadgeOsPrincipal(clueRideSessionDto, clueRideIdentity);
        LOGGER.debug("Instantiating a new Session DTO for {}", emailAddressString);

        Integer memberId = addMember(clueRideSessionDto, clueRideIdentity, emailAddressString);

        if (memberId != null) {
            addInviteAndOuting(clueRideSessionDto, memberId);
        }

        clueRideSessionService.setSessionForToken(accessToken, clueRideSessionDto);
        return clueRideSessionDto;
    }

    private String addBadgeOsPrincipal(ClueRideSessionDto clueRideSessionDto, ClueRideIdentity clueRideIdentity) {
        String emailAddressString = clueRideIdentity.getEmail().toString();
        clueRideSessionDto.setBadgeOSPrincipal(badgeOsPrincipalService.getBadgeOsPrincipal(clueRideIdentity.getEmail()));
        return emailAddressString;
    }

    private Integer addMember(ClueRideSessionDto clueRideSessionDto, ClueRideIdentity clueRideIdentity, String emailAddressString) {
        Integer memberId = null;
        try {
            Member member = memberService.getMemberByEmail(clueRideIdentity.getEmail());
            clueRideSessionDto.setMember(member);
            memberId = member.getId();
        } catch (RecordNotFoundException rnfe) {
            // TODO: CA-409 (maybe throw RuntimeException about data records amiss?)
            LOGGER.error("Unable to find member record for email: {}", emailAddressString);
        }
        return memberId;
    }

    private void addInviteAndOuting(ClueRideSessionDto clueRideSessionDto, Integer memberId) {
        List<Invite> invites = inviteService.getMemberInvites(memberId);
        if (invites.size() > 0) {
            /* The session only cares about the next one coming up. */
            Invite invite = invites.get(0);
            clueRideSessionDto.setInvite(invite);

            /* Check if we can retrieve the Outing. */
            OutingView outingView = outingService.getViewById(invite.getOutingId());
            clueRideSessionDto.setOutingView(outingView);
        }
    }

}
