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
 * Created by jett on 12/2/18.
 */
package com.clueride.auth.session;

import com.clueride.auth.identity.ClueRideIdentity;
import com.clueride.domain.account.member.Member;
import com.clueride.domain.account.principal.BadgeOsPrincipal;
import com.clueride.domain.invite.Invite;
import com.clueride.domain.outing.OutingConstants;
import com.clueride.domain.puzzle.state.PuzzleState;

import java.io.Serializable;

/**
 * "Holder" for session-related class instances.
 */
public class ClueRideSessionDto implements Serializable {
    private ClueRideIdentity clueRideIdentity = null;
    private BadgeOsPrincipal badgeOsPrincipal = null;
    private Member member = null;
    private Invite invite = null;
    private PuzzleState puzzleState = null;
    private Integer outingId = OutingConstants.NO_OUTING;

    public void setClueRideIdentity(ClueRideIdentity clueRideIdentity) {
        this.clueRideIdentity = clueRideIdentity;
    }

    public ClueRideIdentity getClueRideIdentity() {
        return clueRideIdentity;
    }

    public void setBadgeOSPrincipal(BadgeOsPrincipal badgeOSPrincipal) {
        this.badgeOsPrincipal = badgeOSPrincipal;
    }

    public BadgeOsPrincipal getBadgeOsPrincipal() {
        return badgeOsPrincipal;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Invite getInvite() {
        return invite;
    }

    public void setInvite(Invite invite) {
        this.invite = invite;
    }

    public void setPuzzleState(PuzzleState puzzleState) {
        this.puzzleState = puzzleState;
    }

    public PuzzleState getPuzzleState() {
        return puzzleState;
    }

    public Integer getOutingId() {
        return outingId;
    }

    public void setOutingId(Integer outingId) {
        this.outingId = outingId;
    }

    /** Convenience method. */
    public boolean hasNoOuting() {
        return (this.outingId == OutingConstants.NO_OUTING);
    }

}
