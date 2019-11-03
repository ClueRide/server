/*
 * Copyright 2018 Jett Marks
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
 * Created by jett on 9/14/18.
 */
package com.clueride.domain.game;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Defines what we need to track the state of the Game for the clients.
 *
 * Immutable instance is used for broadcasting.
 */
@Immutable
public class GameState {
    private final boolean teamAssembled;
    private final boolean rolling;
    private final boolean outingComplete;
    private final OutingState outingState;
    private final String nextLocationName;
    private final Integer pathIndex;
    private final Integer locationId;
    private final Integer puzzleId;

    private GameState(Builder builder) {
        this.teamAssembled = builder.getTeamAssembled();
        this.rolling = builder.getRolling();
        this.outingComplete = builder.isOutingComplete();
        this.outingState = builder.getOutingState();
        this.nextLocationName = builder.getNextLocation();
        this.pathIndex = builder.getPathIndex();
        this.locationId = builder.getLocationId();
        this.puzzleId = builder.getPuzzleId();
    }

    public boolean getTeamAssembled() {
        return teamAssembled;
    }

    public boolean getRolling() {
        return rolling;
    }

    public boolean isOutingComplete() {
        return outingComplete;
    }

    public OutingState getOutingState() {
        return outingState;
    }

    public String getNextLocationName() {
        return nextLocationName;
    }

    public Integer getPathIndex() {
        return pathIndex;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public Integer getPuzzleId() {
        return puzzleId;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static class Builder {
        /* Initial Game State -- without knowing the Course. */
        private boolean teamAssembled = false;
        private boolean rolling = false;
        private boolean outingComplete = false;
        private OutingState outingState = OutingState.PENDING_ARRIVAL;
        private String nextLocationName = "Meeting Location";
        private int pathIndex = -1;
        private Integer locationId = null;
        private Integer puzzleId = null;

        public GameState build() {
            return new GameState(this);
        }

        public static Builder builder() {
            return new Builder();
        }

        public static Builder from(GameState gameState) {
            return builder()
                    .withTeamAssembled(gameState.getTeamAssembled())
                    .withRolling(gameState.getRolling())
                    .withNextLocationName(gameState.getNextLocationName())
                    .withPathIndex(gameState.getPathIndex())
                    .withLocationId(gameState.getLocationId())
                    .withPuzzleId(gameState.getPuzzleId());
        }

        public Boolean getTeamAssembled() {
            return teamAssembled;
        }

        public Builder withTeamAssembled(Boolean teamAssembled) {
            this.teamAssembled = teamAssembled;
            return this;
        }

        public Boolean getRolling() {
            return rolling;
        }

        public Builder withRolling(Boolean rolling) {
            this.rolling = rolling;
            return this;
        }

        public boolean isOutingComplete() {
            return outingComplete;
        }

        public Builder withOutingComplete(boolean outingComplete) {
            this.outingComplete = outingComplete;
            return this;
        }

        public OutingState getOutingState() {
            /* Value depends on whether we've assembled the team and if we've completed the outing. */
            if (!teamAssembled) {
                this.outingState = OutingState.PENDING_ARRIVAL;
            } else {
                if (this.outingComplete) {
                    this.outingState = OutingState.COMPLETE;
                } else {
                    this.outingState = OutingState.IN_PROGRESS;
                }
            }
            return outingState;
        }

        public Builder withOutingState(OutingState outingState) {
            this.outingState = outingState;
            return this;
        }

        public String getNextLocation() {
            return nextLocationName;
        }

        public Builder withNextLocationName(String nextLocationName) {
            this.nextLocationName = nextLocationName;
            return this;
        }

        public Integer getPathIndex() {
            return pathIndex;
        }

        public Builder withPathIndex(Integer pathIndex) {
            this.pathIndex = pathIndex;
            return this;
        }

        public Builder incrementPathIndex(int moduloValue) {
            /* Game State is set to "Outing Complete" when this wraps; for testing, it's convenient to wrap. */
            return withPathIndex((pathIndex + 1) % moduloValue);
        }

        public Integer getLocationId() {
            return locationId;
        }

        public Builder withLocationId(Integer locationId) {
            this.locationId = locationId;
            return this;
        }

        public Integer getPuzzleId() {
            return puzzleId;
        }

        public Builder withPuzzleId(Integer puzzleId) {
            this.puzzleId = puzzleId;
            return this;
        }
    }

}
