/*
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
 * Created by jett on 7/25/17.
 */
package com.clueride.auth.filter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Priority;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;

import com.clueride.RecordNotFoundException;
import com.clueride.auth.ClueRideSession;
import com.clueride.auth.Secured;
import com.clueride.auth.access.AccessTokenService;
import com.clueride.auth.identity.ClueRideIdentity;
import com.clueride.config.ConfigService;
import com.clueride.domain.account.principal.PrincipalService;

/**
 * Allows picking up Authorization headers and extracting the Principal.
 *
 * This is also responsible for inserting the Principal into the Session.
 * The @Secured annotation on Jersey endpoints is what triggers this to be called.
 *
 * TODO: CA-410 Consider moving some of this logic over to the AccessState service.
 */
@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    @Inject
    private Logger LOGGER;

    @Inject
    @ClueRideSession
    private Event<String> accessTokenEvent;

    @Inject
    private PrincipalService principalService;

    @Inject
    private AccessTokenService accessTokenService;

    @Inject
    private ConfigService configService;

    private static Map<String, String> testAccountMap = null;

    /**
     * Using the @Provider annotation is forcing this to use the no-parameter constructor.
     *
     * Injected components are not available when this is constructed.
     */
    @Inject
    public AuthenticationFilter() {
        super();
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        /* Get the HTTP Authorization header from the request. */
        String authorizationHeader =
                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        /* Check if the HTTP Authorization header is present and formatted correctly. */
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            LOGGER.error("Authorization Header missing or malformed");
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        /* Extract the token from the HTTP Authorization header. */
        String token = authorizationHeader.substring("Bearer".length()).trim();

        /* Lazy init */
        if (testAccountMap == null) {
            testAccountMap = configService.getTestAccountMap();
        }

        String candidatePrincipalName;
        if (token.equals(configService.getTestToken())) {
            candidatePrincipalName = configService.getTestAccount();
            buildClueRideIdentity(token, candidatePrincipalName);
        } else if (testAccountMap.containsKey(token)) {
            candidatePrincipalName = testAccountMap.get(token);
            buildClueRideIdentity(token, candidatePrincipalName);
        } else {
            try {
                candidatePrincipalName = accessTokenService.getPrincipalString(token);
            } catch (RecordNotFoundException rnfe) {
                LOGGER.info("Authorization Header (\"{}\") not validated by Identity Provider", token);
                requestContext.abortWith(
                        Response.status(Response.Status.UNAUTHORIZED).build());
                return;
            }
        }

        LOGGER.debug("Logged in as {}", candidatePrincipalName);
        accessTokenEvent.fire(token);
    }

    /**
     * Given a token (expected to match this environment's "test" token), create a
     * matching ClueRideIdentity for use during testing.
     * @param token as configured.
     * @param candidatePrincipalName also as configured.
     */
    private void buildClueRideIdentity(String token, String candidatePrincipalName) throws MalformedURLException {
        ClueRideIdentity clueRideIdentity;
        clueRideIdentity = ClueRideIdentity.Builder.builder()
                .withSub("")
                .withDisplayName("Test Account")
                .withNickName("scarf")
                .withPictureUrl("https://clueride.com")
                .withLocale("en")
                .withUpdatedAt(new Date())
                .withEmailVerified(true)
                .withEmailString(candidatePrincipalName).build();
        accessTokenService.addIdentity(token, clueRideIdentity);
    }

}
