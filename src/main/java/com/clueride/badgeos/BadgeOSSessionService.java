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

import org.apache.http.client.CookieStore;

/**
 * Defines operations against the BadgeOS Session.
 */
public interface BadgeOSSessionService {

    /**
     * Recognize an Achievement for the User.
     *
     * This is the beginning of an asynchronous operation that will eventually
     * either be successful or not.
     *
     * @param userId Word Press unique identifier.
     * @param achievementId Corresponds to a specific Step or Badge within BadgeOS.
     */
    void awardAchievement(Integer userId, Integer achievementId);

    /**
     * Refreshes the session cookies for BadgeOS sessions.
     */
    void refreshSession();

    /**
     * Retrieves a Nonce from BadgeOS suitable for awarding achievements.
     * @return String representation of the Nonce for Awarding achievements.
     */
    Nonce getAwardNonce();

    /**
     * Retrieves a Nonce from BadgeOS suitable for revoking an existing achievement.
     * @return String representation of the Nonce for Deletion.
     */
    Nonce getRevokeNonce();

    /**
     * Retrieves the set of BadgeOS Session cookies.
     * Generally used for diagnostics; provides sensitive information.
     * @return CookieStore from having established a BadgeOS Session.
     */
    CookieStore getSessionCookies();

}
