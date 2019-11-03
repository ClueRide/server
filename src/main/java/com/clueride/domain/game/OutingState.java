package com.clueride.domain.game;/*
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
 * Created by jett on 11/3/19.
 */

import java.util.HashMap;
import java.util.Map;

public enum OutingState {
    PENDING_ARRIVAL("pending-arrival"),
    IN_PROGRESS("in-progress"),
    COMPLETE("complete");

    public final String eventName;

    private static final Map<String, OutingState> BY_STATE_NAME = new HashMap<>();

    static {
        for (OutingState m : values()) {
            BY_STATE_NAME.put(m.eventName, m);
        }
    }

    OutingState(String eventName) {
        this.eventName = eventName;
    }

    public static OutingState fromMethodName(String methodName) {
        return BY_STATE_NAME.get(methodName);
    }

}
