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
 * Created by jett on 1/16/18.
 */
package com.clueride.domain.badge;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clueride.auth.session.ClueRideSession;
import com.clueride.auth.session.ClueRideSessionDto;
import com.clueride.domain.account.principal.BadgeOsPrincipal;

/**
 * Default Implementation of BadgeService.
 */
public class BadgeServiceImpl implements BadgeService {
    @Inject
    private Logger LOGGER;

    @Inject
    @SessionScoped
    @ClueRideSession
    private ClueRideSessionDto clueRideSessionDto;

    @Inject
    private BadgeStore badgeStore;

    @Inject
    private BadgeTypeService badgeTypeService;

    @Override
    public List<BadgeFeatures> getBadges() {
        BadgeOsPrincipal badgeOsPrincipal = clueRideSessionDto.getBadgeOsPrincipal();
        Integer userId = badgeOsPrincipal.getBadgeOsUserId();
        List<BadgeFeatures> badgeFeaturesList = new ArrayList<>();
        LOGGER.info("Looking up Badges for User ID " + userId);
        List<BadgeFeaturesEntity> builderList = badgeStore.getAwardedBadgesForUser(userId);
        for (BadgeFeaturesEntity builder : builderList) {
            badgeFeaturesList.add(builder.build());
        }
        return badgeFeaturesList;
    }

}
