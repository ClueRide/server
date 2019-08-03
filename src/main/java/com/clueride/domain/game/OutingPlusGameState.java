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
 * Created by jett on 7/25/19.
 */
package com.clueride.domain.game;

import javax.annotation.concurrent.Immutable;

import com.clueride.domain.outing.OutingView;

/**
 * Record of both the Game State and the OutingView for recording Badge Events.
 */
@Immutable
public class OutingPlusGameState {
    private final GameState gameState;
    private final OutingView outingView;


    public OutingPlusGameState(
            GameState gameState,
            OutingView outingView
    ) {
        this.gameState = gameState;
        this.outingView = outingView;
    }

    public GameState getGameState() {
        return gameState;
    }

    public OutingView getOutingView() {
        return outingView;
    }

}
