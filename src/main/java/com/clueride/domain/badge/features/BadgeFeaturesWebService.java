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
 * Created by jett on 6/15/19.
 */
package com.clueride.domain.badge.features;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.clueride.auth.Secured;

/**
 * WebService for BadgeFeatures.
 */
@Path("/badge-features")
public class BadgeFeaturesWebService {
    @Inject
    private BadgeFeaturesService badgeFeaturesService;

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<BadgeFeatures> getAllBadgeFeatures() {
        return badgeFeaturesService.getAllBadgeFeatures();
    }

    @GET
    @Path("themed")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<BadgeFeatures> getThemedBadgeFeatures() {
        return badgeFeaturesService.getThemedBadgeFeatures();
    }

}
