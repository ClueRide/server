package com.clueride.domain.badge.features;/*
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
 * Created by jett on 5/28/19.
 */

/**
 * Defines the badge levels.
 *
 * Colors for these levels are described on this page: http://bikehighways.wikidot.com/badge-levels.
 */
public enum ALevel {
    NO_LEVEL,
    AWARE,
    ADEPT,
    ADVOCATE,
    ANGEL;

    public static ALevel toALevel(String value) {
        ALevel aLevel;
        try {
            aLevel = valueOf(value.toUpperCase());
        } catch (IllegalArgumentException iae) {
            aLevel = NO_LEVEL;
        }
        return aLevel;
    }

}
