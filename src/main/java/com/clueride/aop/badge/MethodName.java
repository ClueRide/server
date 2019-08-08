package com.clueride.aop.badge;/*
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
 * Created by jett on 7/26/19.
 */

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the recognized values for Badge Event Method Names.
 */
public enum MethodName {

    ARRIVAL("arrival"),
    ADD_NEW_TO_LOCATION("addNewToLocation"),    // New Image
    COURSE_COMPLETED("courseCompleted"),
    POST_ANSWER_FOR_SESSION("postAnswerForSession"),
    REGISTER("register"),
    TEAM_ASSEMBLED("teamAssembled"),
    UPDATE_LOCATION("updateLocation"),
    UPLOAD_IMAGE("uploadImage"),
    ;

    public final String methodName;

    private static final Map<String,MethodName> BY_METHOD_NAME = new HashMap<>();

    static {
        for (MethodName m : values()) {
            BY_METHOD_NAME.put(m.methodName, m);
        }
    }

    MethodName(String methodName) {
        this.methodName = methodName;
    }

    public static MethodName fromMethodName(String methodName) {
        return BY_METHOD_NAME.get(methodName);
    }

}
