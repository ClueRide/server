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

import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.NoResultException;

import org.slf4j.Logger;

import com.clueride.RecordNotFoundException;
import com.clueride.aop.badge.BadgeCapture;
import com.clueride.auth.identity.ClueRideIdentity;

/**
 * Implementation of {@link MemberService}.
 */
public class MemberServiceImpl implements MemberService {
    @Inject
    private Logger LOGGER;

    private final MemberStore memberStore;

    @Inject
    public MemberServiceImpl(MemberStore memberStore) {
        this.memberStore = memberStore;
    }

    @Override
    @BadgeCapture
    /* TODO: Testing only; not an actual call that needs BadgeCapture. */
    public List<Member> getAllMembers() {
        LOGGER.debug("Requesting All Members");
        List<Member.Builder> builders = memberStore.getAllMembers();
        List<Member> members = new ArrayList<>();
        for (Member.Builder builder : builders) {
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
        Member.Builder builder = memberStore.getMemberByEmail(email);
        return builder.build();
    }

    @Override
    public Member getMemberByEmail(InternetAddress emailAddress) {
        try {
            Member.Builder builder = memberStore.getMemberByEmail(emailAddress);
            return builder.build();
        } catch (NoResultException nre) {
            throw new RecordNotFoundException("criteria: " + emailAddress.getAddress());
        }
    }

    @Override
    // TODO: CA-405 connect this more tightly
    public Member createNewMember(ClueRideIdentity clueRideIdentity) {
        Member.Builder memberBuilder = memberStore.addNew(
                Member.Builder.from(clueRideIdentity)
        );
        return memberBuilder.build();
    }

}
