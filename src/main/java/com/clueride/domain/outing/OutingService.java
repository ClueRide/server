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
 * Created by jett on 1/1/19.
 */
package com.clueride.domain.outing;

/**
 * Defines operations on an Outing.
 */
public interface OutingService {
    /**
     * Given an ID, retrieve the Outing identified by that ID.
     * @param outingId Unique integer representing the Outing.
     * @return Matching Outing.
     */
    Outing getById(Integer outingId);

    /**
     * Given an ID, retrieve the {@link OutingView} identified by that ID.
     * @param outingId Unique integer representing the OutingView.
     * @return Matching OutingView.
     */
    OutingView getViewById(Integer outingId);

    /**
     * Once the session is established, we'll know which Outing the player will be engaging; this retrieves that
     * {@link OutingView}.
     * @return the Active Outing which is the one within the player's session.
     */
    OutingView getActiveOutingView();

}
