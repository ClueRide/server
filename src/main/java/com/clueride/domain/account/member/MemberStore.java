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
 * Created by jett on 6/25/16.
 */
package com.clueride.domain.account.member;

import java.util.List;

import javax.mail.internet.InternetAddress;

/**
 * Persistence interface for {@link Member} instances.
 */
public interface MemberStore {
    /**
     * Accepts fully-populated {@link MemberEntity} to the store and returns the Member record with DB-assigned ID.
     * @param memberEntity - instance to be persisted.
     * @return Same record as passed in with DB-assigned ID.
     */
    MemberEntity addNew(MemberEntity memberEntity);

    /**
     * Retrieves the {@link Member} instance matching the ID.
     * @param id - Unique ID for the Member.
     * @return Unique matching instance.
     */
    MemberEntity getMemberById(Integer id);

    /**
     * Retrieves the list of {@link Member} whose name matches.
     * Note that names are not checked for uniqueness; this won't be the PK field
     * for the record.
     * @param name - String account name for the Member.
     * @return All matching instances.
     */
    List<MemberEntity> getMemberByName(String name);

    /**
     * Retrieves the {@link Member} instance matching the emailAddress.
     * @param emailAddress - InternetAddress for the member.
     * @return Unique matching instance.
     */
    MemberEntity getMemberByEmail(InternetAddress emailAddress);

    /**
     * Updates an existing record with the contents of this Member instance.
     * @param member Instance with new information.
     */
    void update(Member member);

    /**
     * Returns the entire list of members contained within the store.
     * TODO: This won't be sustainable once we have a significant number of members, but it's sufficient for testing.
     * @return List of the Members currently defined.
     */
    List<MemberEntity> getAllMembers();

    /**
     * Given a string pattern, find member records that contain the pattern.
     * @param pattern String pattern to be matched.
     * @return List of {@link MemberEntity} records.
     */
    List<MemberEntity> getMatchingMembers(String pattern);

}
