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
import java.security.Principal;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;

import com.clueride.auth.Secured;
import com.clueride.auth.access.AccessTokenService;
import com.clueride.config.ConfigService;

/**
 * Allows picking up Authorization headers and extracting the Principal.
 *
 * This is also responsible for inserting the Principal into the Session.
 * The @Secured annotation on Jersey endpoints is what triggers this to be called.
 */
@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {
    @Inject
    private Logger LOGGER;

    // TODO: Maybe provided by CDI?
//    private final Provider<SessionPrincipal> sessionPrincipalProvider;
    // TODO: Perhaps a better place for this? What functionality does this provide?
//    private final PrincipalService principalService;
    @Inject
    private AccessTokenService accessTokenService;

    @Inject
    private ConfigService configService;

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

        if (requestContext.getMethod().equals(HttpMethod.OPTIONS)) {
            /* Free pass for OPTIONS requests? */
            LOGGER.debug("Allowing OPTIONS request");
            return;
        }
        // Get the HTTP Authorization header from the request
        String authorizationHeader =
                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Check if the HTTP Authorization header is present and formatted correctly
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            LOGGER.error("Authorization Header missing or malformed");
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        // Extract the token from the HTTP Authorization header
        String token = authorizationHeader.substring("Bearer".length()).trim();

        String candidatePrincipalName = "Not logged in";
        if (token.equals(configService.getTestToken())) {
            candidatePrincipalName = configService.getTestAccount();
        } else {
            candidatePrincipalName = accessTokenService.getPrincipalString(token);
        }

        final String principalName = candidatePrincipalName;
        LOGGER.info("Logged in as " + principalName);

        /* This one is used for Method Interceptors for Badge Capture. */
//        setSessionPrincipal(principalName);

        /* This one is used for Jersey Calls. */
        setSecurityContextPrincipal(requestContext, principalName);
    }

    /** Add user to the Context for this invocation; used by JAX-RS & Jersey calls. */
    private void setSecurityContextPrincipal(
            ContainerRequestContext requestContext,
            final String principalName
    ) {
        final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
        requestContext.setSecurityContext(new SecurityContext() {

            @Override
            public Principal getUserPrincipal() {

                return new Principal() {

                    @Override
                    public String getName() {
                        return principalName;
                    }
                };
            }

            @Override
            public boolean isUserInRole(String role) {
                return true;
            }

            @Override
            public boolean isSecure() {
                return currentSecurityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return "Bearer";
            }
        });
    }

    /** Name the user's principal as the SessionPrincipal. */
//    private void setSessionPrincipal(String principalName) {
//        sessionPrincipalProvider.get().setSessionPrincipal(
//                principalService.getPrincipalForEmailAddress(
//                        principalName
//                )
//        );
//    }

}
