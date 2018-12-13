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
 * Created by jett on 12/9/18.
 */
package com.clueride.auth.state;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHeaders;

/**
 * REST API for certain operations on an AccessToken's state.
 *
 * This verifies that the AccessToken which is presented by the client can be used or needs to be refreshed.
 *
 * Obtaining an AccessToken is the responsibility of a 3rd-party identity provider.
 */
@Path("access/state")
@RequestScoped
public class AccessStateWebService {
    @Inject
    private AccessStateService accessStateService;

    @Inject HttpServletRequest httpServletRequest;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean getAccessTokenState() {
        String authHeader = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        return accessStateService.isRegistered(authHeader);
    }

}
