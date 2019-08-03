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

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.NoResultException;

import org.slf4j.Logger;

import com.clueride.RecordNotFoundException;
import com.clueride.auth.identity.ClueRideIdentity;
import com.clueride.auth.session.ClueRideSession;
import com.clueride.auth.session.ClueRideSessionDto;

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

    @Inject
    public MemberServiceImpl(MemberStore memberStore) {
        this.memberStore = memberStore;
    }

    @Override
    public Member getActiveMember() {
        return clueRideSessionDto.getMember();
    }

    @Override
    public List<Member> getAllMembers() {
        LOGGER.debug("Requesting All Members");
        List<MemberBuilder> builders = memberStore.getAllMembers();
        List<Member> members = new ArrayList<>();
        for (MemberBuilder builder : builders) {
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
        MemberBuilder builder = memberStore.getMemberByEmail(email);
        return builder.build();
    }

    @Override
    public Member getMemberByEmail(InternetAddress emailAddress) {
        try {
            MemberBuilder builder = memberStore.getMemberByEmail(emailAddress);
            return builder.build();
        } catch (NoResultException nre) {
            throw new RecordNotFoundException("criteria: " + emailAddress.getAddress());
        }
    }

    @Override
    // TODO: CA-405 connects the Image URL more tightly (SVR-37 addresses this in part).
    public Member createNewMember(ClueRideIdentity clueRideIdentity) {
        MemberBuilder memberBuilder = memberStore.addNew(
                MemberBuilder.from(clueRideIdentity)
        );
        return memberBuilder.build();
    }

    @Override
    public List<Member> getMatchingMembers(String pattern) {
        List<Member> members = new ArrayList<>();
        List<MemberBuilder> memberBuilders = memberStore.getMatchingMembers(pattern);
        for (MemberBuilder builder : memberBuilders) {
            members.add(builder.build());
        }
        return members;
    }

}
