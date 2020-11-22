package com.clueride.auth.state;/*
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
 * Created by jett on 12/9/18.
 */

import com.clueride.RecordNotFoundException;
import com.clueride.auth.access.AccessTokenService;
import com.clueride.auth.identity.ClueRideIdentity;
import com.clueride.config.ConfigService;
import com.clueride.domain.account.member.MemberService;
import com.clueride.domain.account.principal.BadgeOsPrincipal;
import com.clueride.domain.account.principal.BadgeOsPrincipalService;
import com.clueride.util.TestOnly;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.NgCdiRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.testng.Assert.*;

/**
 * Exercises the AccessStateServiceImplTest class.
 */
@AdditionalClasses({TestResources.class})
public class AccessStateServiceImplTest extends NgCdiRunner {

    @Produces
    @Mock
    @TestOnly
    Logger LOGGER;

    private String TEST_TOKEN = "Test Token";
    private String AUTH_HEADER_TEST = "Bearer " + TEST_TOKEN;
    private String IDENTITY_PROVIDER_TOKEN = "1qaz2wsx";
    private String AUTH_HEADER_IP = "Bearer " + IDENTITY_PROVIDER_TOKEN;

    @Singleton
    @Produces
    @Mock
    @TestOnly
    private AccessTokenService accessTokenService;

    @Produces
    @Mock
    @TestOnly
    private BadgeOsPrincipalService badgeOsPrincipalService;

    @Inject
    private Instance<ClueRideIdentity> clueRideIdentityProvider;
    private ClueRideIdentity mockClueRideIdentity;

    @Inject
    private Instance<BadgeOsPrincipal> badgeOsPrincipalProvider;
    private BadgeOsPrincipal mockBadgeOsPrincipal;

    @Produces
    @Mock
    @TestOnly
    private ConfigService configService;

    @Produces
    @Mock
    @TestOnly
    private MemberService memberService;

    @InjectMocks
    private AccessStateServiceImpl toTest;

    @BeforeSuite
    /* Initializing the mocks once per suite avoids creating new instances of the mocks on each test run. */
    public void suiteSetup() throws Exception {
        initMocks(this);
    }

    @BeforeMethod
    public void setUp() throws Exception {
        assertNotNull(toTest);

        /* Config service should always return this. */
        when(configService.getTestToken()).thenReturn(TEST_TOKEN);

        mockClueRideIdentity = clueRideIdentityProvider.get();
        assertNotNull(mockClueRideIdentity);
        mockBadgeOsPrincipal = badgeOsPrincipalProvider.get();
        assertNotNull(mockBadgeOsPrincipal);
    }

    @AfterMethod
    public void tearDown() throws Exception {
    }

    /** Should allow token that has already been activated. */
    @Test
    public void testIsRegistered_activeSession() throws Exception {
        /* setup test */
        /* train mocks */
        when(accessTokenService.isSessionActive(IDENTITY_PROVIDER_TOKEN)).thenReturn(true);

        /* make call */
        /* verify results */
        assertTrue(toTest.isRegistered(AUTH_HEADER_IP));
    }

    /** Should allow token found by identity provider. */
    @Test
    public void testIsRegistered_inactiveSession() throws Exception {
        /* setup test */
        assertNotNull(mockClueRideIdentity);
        assertNotNull(mockBadgeOsPrincipal);

        /* train mocks */
        when(accessTokenService.isSessionActive(IDENTITY_PROVIDER_TOKEN)).thenReturn(false);
        /* simulate finding the identity */
        when(accessTokenService.getIdentity(IDENTITY_PROVIDER_TOKEN)).thenReturn(mockClueRideIdentity);
        /* simulate finding that identity in our Badge OS DB */
        when(badgeOsPrincipalService.getBadgeOsPrincipal(mockClueRideIdentity.getEmail()))
                .thenReturn(mockBadgeOsPrincipal);

        /* make call */
        /* verify results */
        assertTrue(toTest.isRegistered(AUTH_HEADER_IP));
    }

    /** Should forbid token that isn't test token or not found by identity provider. */
    @Test
    public void testIsRegistered_inactiveSession_notFound() throws Exception {
        /* setup test */
        /* train mocks */
        when(accessTokenService.isSessionActive(IDENTITY_PROVIDER_TOKEN)).thenReturn(false);
        doThrow(new RecordNotFoundException()).when(accessTokenService).getIdentity(IDENTITY_PROVIDER_TOKEN);

        /* make call */
        /* verify results */
        assertFalse(toTest.isRegistered(AUTH_HEADER_IP));
    }

    // TODO: CA-405 is expected to expand this list to further cover the module under test.

    /** Should allow valid Test Token. */
    @Test
    public void testIsRegistered_testToken() throws Exception {
        /* setup test */
        /* train mocks */
        when(accessTokenService.isSessionActive(TEST_TOKEN)).thenReturn(false);
        when(configService.getTestToken()).thenReturn(TEST_TOKEN);

        /* make call */
        /* verify results */
        assertTrue(toTest.isRegistered(AUTH_HEADER_TEST));
    }

    /** Should forbid null token. */
    @Test
    public void testIsRegistered_null() throws Exception {
        assertFalse(toTest.isRegistered(null));
    }

    /** Should forbid empty token. */
    @Test
    public void testIsRegistered_empty() throws Exception {
        assertFalse(toTest.isRegistered(""));
    }

    /** Should forbid malformed token. */
    @Test
    public void testIsRegistered_malformed() throws Exception {
        assertFalse(toTest.isRegistered(TEST_TOKEN));
    }

}
