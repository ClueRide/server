/*
 * Copyright 2016 Jett Marks
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
 * Created by jett on 12/29/18.
 */
package com.clueride.domain.invite;

import java.io.IOException;
import java.util.List;

/**
 * Persistence interface for {@link Invite} instances.
 */
public interface InviteStore {
    /**
     * Accepts fully-populated Invite and returns the Integer ID; the token should already have been populated.
     * Integer ID is the DB unique identifier; the token is a hashed value serving as a public ID.
     * @param builder - Builder for Instance to be persisted.
     * @return Integer DB-generated ID.
     * @throws IOException if unable to access underlying backing store.
     */
    Integer addNew(Invite.Builder builder) throws IOException;

    /**
     * Returns a list of fully-populated Invitations for the given Outing.
     * @param outingId - Unique ID of the Outing which may or may not have Invitations yet.
     * @return List of Invitations or empty list if none created yet.
     */
    List<Invite.Builder> getInvitationsByOuting(Integer outingId);

    /**
     * Given a memberId, retrieve the list of Active Invitations in order of soonest to latest.
     * @param memberId - Unique identifier for the Member who is being invited.
     * @return List of Invitations for the Member, in order of sooner rather than later.
     */
    List<Invite.Builder> getUpcomingInvitationsByMemberId(Integer memberId);

    /**
     * Given an invitation ID, retrieve the matching instance.
     * @param inviteId Unique identifier for the Invite.
     * @return Matching Invite Builder.
     */
    Invite.Builder getInvitationById(Integer inviteId);

    /**
     * Given an instance of Invite.Builder, persist (or create a new record).
     * @param builder instance with new information to replace existing or create new.
     * @return The same builder with populated ID if this is a new record.
     */
    Invite.Builder save(Invite.Builder builder);

}
