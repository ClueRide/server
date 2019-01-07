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
 * Created by jett on 8/28/18.
 */
package com.clueride.domain.account.principal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;


import org.slf4j.Logger;

import com.clueride.auth.ClueRideSession;
import com.clueride.auth.ClueRideSessionDto;
import static java.util.Objects.requireNonNull;

/**
 * Implementation that simply maps between what is in BadgeOS for a given email address.
 */
public class BadgeOsPrincipalServiceImpl implements BadgeOsPrincipalService, Serializable {
    @Inject
    private Logger LOGGER;

    @Inject
    @SessionScoped
    @ClueRideSession
    private ClueRideSessionDto clueRideSessionDto;

    private final BadgeOsPrincipalStore badgeOsPrincipalStore;

    @Inject
    public BadgeOsPrincipalServiceImpl(
            BadgeOsPrincipalStore badgeOsPrincipalStore
    ) {
        this.badgeOsPrincipalStore = badgeOsPrincipalStore;
    }

    @Override
    public BadgeOsPrincipal getBadgeOsPrincipal(InternetAddress emailAddress) {
        requireNonNull(emailAddress, "Email Address must be non-null");
        LOGGER.debug("Looking up Principal for {}", emailAddress.toString());

        return badgeOsPrincipalStore.getBadgeOsPrincipalForEmailAddress(
                emailAddress
        ).build();
    }

    @Override
    public BadgeOsPrincipal getBadgeOsPrincipal(String emailAddress) {
        requireNonNull(emailAddress, "Email Address must be non-null");

        try {
            InternetAddress internetAddress = new InternetAddress(emailAddress);
            return getBadgeOsPrincipal(internetAddress);
        } catch (AddressException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BadgeOsPrincipal> getFilteredPrincipals(BadgeOsPrincipalFilter filter) {
        List<BadgeOsPrincipal> principals = new ArrayList<>();
        List<BadgeOsPrincipal.Builder> builders = badgeOsPrincipalStore.getAll();
        for (BadgeOsPrincipal.Builder builder : builders) {
            principals.add(builder.build());
        }
        return principals;
    }

    @Override
    public BadgeOsPrincipal getCurrentSessionPrincipal() {
        return clueRideSessionDto.getBadgeOsPrincipal();
    }

}
