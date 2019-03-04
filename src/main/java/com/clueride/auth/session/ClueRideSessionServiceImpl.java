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

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of {@link ClueRideSessionService} backed by a simple Map.
 *
 * TODO: This needs to be reconstructed so it's members can be expired.
 */
public class ClueRideSessionServiceImpl implements ClueRideSessionService {
    private static Map<String, ClueRideSessionDto> sessionMap = new HashMap<>();

    @Override
    public ClueRideSessionDto getSessionFromToken(String token) {
        return sessionMap.get(token);
    }

    @Override
    public void setSessionForToken(String token, ClueRideSessionDto sessionDto) {
        sessionMap.put(token, sessionDto);
    }

}
