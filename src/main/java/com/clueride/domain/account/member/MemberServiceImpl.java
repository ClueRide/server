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
 * Created by jett on 11/12/18.
 */
package com.clueride.domain.account.member;

import com.clueride.RecordNotFoundException;
import com.clueride.auth.identity.ClueRideIdentity;
import com.clueride.auth.session.ClueRideSession;
import com.clueride.auth.session.ClueRideSessionDto;
import com.clueride.domain.account.register.RegisterService;
import org.slf4j.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link MemberService}.
 */
public class MemberServiceImpl implements MemberService {
    @Inject
    private Logger LOGGER;

    @Inject
    @SessionScoped
    @ClueRideSession
    private ClueRideSessionDto clueRideSessionDto;

    private final MemberStore memberStore;
    private final RegisterService registerService;

    @Inject
    public MemberServiceImpl(
            MemberStore memberStore,
            RegisterService registerService
    ) {
        this.memberStore = memberStore;
        this.registerService = registerService;
    }

    @Override
    public Member getActiveMember() {
        return clueRideSessionDto.getMember();
    }

    @Override
    public List<Member> getAllMembers() {
        LOGGER.debug("Requesting All Members");
        List<MemberEntity> builders = memberStore.getAllMembers();
        List<Member> members = new ArrayList<>();
        for (MemberEntity builder : builders) {
            members.add(builder.build());
        }
        return members;
    }

    @Override
    public Member getMember(Integer id) {
        LOGGER.debug("Requesting member with ID: {}", id);
        return memberStore.getMemberById(id).build();
    }

    @Override
    public Member getMemberByEmail(String emailAddress) throws AddressException {
        LOGGER.debug("Requesting Member by email " + emailAddress);
        InternetAddress email = new InternetAddress(emailAddress);
        MemberEntity builder = memberStore.getMemberByEmail(email);
        return builder.build();
    }

    @Override
    public Member getMemberByEmail(InternetAddress emailAddress) {
        try {
            MemberEntity builder = memberStore.getMemberByEmail(emailAddress);
            return builder.build();
        } catch (NoResultException nre) {
            throw new RecordNotFoundException("criteria: " + emailAddress.getAddress());
        }
    }

    @Override
    public MemberEntity createNewMember(ClueRideIdentity clueRideIdentity) {
        return memberStore.addNew(
                MemberEntity.from(clueRideIdentity)
        );
    }

    @Override
    public List<Member> getMatchingMembers(String pattern) {
        List<Member> members = new ArrayList<>();
        List<MemberEntity> memberEntities = memberStore.getMatchingMembers(pattern);
        for (MemberEntity builder : memberEntities) {
            members.add(builder.build());
        }
        return members;
    }

    /**
     * This is called by clients when they believe the profile for an account is
     * either brand new or has updated information.
     *
     * The Member record supplied should correspond to the one that is placed in
     * the session during the API's check for the access token.
     *
     * This implementation performs a brief verification that the email addresses
     * match up. This is also the opportunity to update / create the BadgeOS account
     * if it doesn't exist.
     *
     * This does invoke the registration of the device, but should only do so if the
     * account is new (no BadgeOS record; we can't award the badge without a BadgeOS
     * record and once we have that record, we shouldn't award the badge again).
     *
     * @param clientSideMember to be checked.
     * @param authHeader
     * @return same (or updated?) member.
     */
    @Override
    public Member crossCheck(Member clientSideMember, String authHeader) {
        requireNonNull(clientSideMember, "Unable to run cross check without Member");

        /* Creation of New Members has been moved to the AccessStateService. */

        return clientSideMember;
    }

}
