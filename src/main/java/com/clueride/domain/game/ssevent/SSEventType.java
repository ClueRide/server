package com.clueride.domain.game.ssevent;/*
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
 * Created by jett on 3/26/19.
 */

/** Defines the set of Server-Sent Event Types. */
public enum SSEventType {
    GAME_STATE,
    ANSWER_SUMMARY,
    TETHER
    // TODO: SVR-73 add Badge Event to match the one introduced by SSE-13.

}
