/*
 * Copyright 2019 Jett Marks
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
 * Created by jett on 7/20/19.
 */
package com.clueride.badgeos;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.clueride.auth.Secured;

/**
 * Temporary web interface for exercising the BadgeOSSessionService.
 */
@Path("/badgeos")
public class BadgeOSWebService {
    @Inject
    private BadgeOSSessionService badgeOSSessionService;

    @GET
    @Secured
    @Path("award-nonce")
    @Produces(MediaType.APPLICATION_JSON)
    public Nonce getAwardNonce() {
        return badgeOSSessionService.getAwardNonce();
    }

    @GET
    @Secured
    @Path("revoke-nonce")
    @Produces(MediaType.APPLICATION_JSON)
    public Nonce getRevokeNonce() {
        return badgeOSSessionService.getRevokeNonce();
    }

}
