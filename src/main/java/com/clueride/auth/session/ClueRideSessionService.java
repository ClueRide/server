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
 * Created by jett on 2/28/19.
 */
package com.clueride.auth.session;

/**
 * Defines operations on Clue Ride Sessions held in cache.
 */
public interface ClueRideSessionService {
    /**
     * Given an properly formatted token, retrieve the mapped Session object.
     *
     * @param token unique and opaque token representing the user's session.
     * @return Matching Session DTO holding the session state.
     */
    ClueRideSessionDto getSessionFromToken(String token);

    /**
     * Maintains the session information for a given token.
     * @param token unique and opaque token representing the user's session.
     * @param sessionDto Session DTO holding the session state.
     */
    void setSessionForToken(String token, ClueRideSessionDto sessionDto);

}
