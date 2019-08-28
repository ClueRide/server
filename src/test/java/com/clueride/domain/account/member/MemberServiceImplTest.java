package com.clueride.domain.account.member;/*
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
 * Created by jett on 10/7/17.
 */

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.NgCdiRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.clueride.util.TestOnly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Exercises the MemberServiceImpl class.
 */
@AdditionalClasses({TestResources.class})
public class MemberServiceImplTest extends NgCdiRunner {

    private InternetAddress goodAddress;

    @Produces
    @Mock
    private MemberStore memberStore;

    @Produces
    @Mock
    Logger LOGGER;

    @Inject
    @TestOnly
    private Member member;

    @InjectMocks
    private MemberServiceImpl toTest;

    @BeforeMethod
    public void setUp() throws Exception {
        initMocks(this);
//        toTest = new MemberServiceImpl(memberStore);
        assertNotNull(member);
        goodAddress = new InternetAddress(member.getEmailAddress());
        assertNotNull(toTest);
        LOGGER.debug("This Logger is able to be injected");
    }

    @Test
    public void testGetMember() throws Exception {
        LOGGER.debug("This Logger is able to be injected");
    }

    @Test
    public void testGetMemberByDisplayName() throws Exception {
        LOGGER.debug("This Logger is able to be injected");
    }

    @Test
    public void testGetMemberByEmail() throws Exception {
        Member expected = member;
        assertNotNull(memberStore);
        when(memberStore.getMemberByEmail(any(InternetAddress.class))).thenReturn(MemberEntity.from(expected));

        Member actual = toTest.getMemberByEmail(goodAddress.getAddress());
        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = AddressException.class)
    public void testGetMemberByEmail_badEmail() throws Exception {
        toTest.getMemberByEmail("");
    }

    @Test
    public void testCreateNewMemberWithEmail() throws Exception {
        LOGGER.debug("This Logger is able to be injected");
    }

    @Test
    public void testGetAllMembers() throws Exception {
        LOGGER.debug("This Logger is able to be injected");
    }

}