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


import javax.annotation.concurrent.Immutable;
import javax.inject.Inject;

import com.clueride.config.ConfigService;
import static java.util.Objects.requireNonNull;

/**
 * Values used to populate the Login Form for BadgeOS.
 *
 * Sensitive information is kept in configuration files outside
 * of the VCS.
 */
@Immutable
public class BadgeOSCredentials {
    private final String log;
    private final String pwd;
    private final static String WP_SUBMIT = "Log In";

    @Inject
    public BadgeOSCredentials(
            ConfigService configService
    ) {
        this.log = requireNonNull(configService.getBadgeOSAccountName());
        this.pwd = requireNonNull(configService.getBadgeOSPassword());
    }

    public String getLog() {
        return log;
    }

    public String getPwd() {
        return pwd;
    }

    public String getWpSubmit() {
        return WP_SUBMIT;
    }

}
