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
 * Created by jett on 11/12/18.
 */
package com.clueride.domain.account.member;

import com.clueride.auth.identity.ClueRideIdentity;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.List;

/**
 * Provides business-layer services for Clue Ride Members.
 */
public interface MemberService {

    /**
     * From the session, returns the Active {@link Member} instance.
     * @return Member instance from the Session.
     */
    Member getActiveMember();

    /**
     * Returns the entire list of members contained within the store.
     * TODO: This won't be sustainable once we have a significant number of members, but it's sufficient for testing.
     * @return List of the Members currently defined.
     */
    List<Member> getAllMembers();

    /**
     * Given a Member's ID, retrieve the matching record.
     * @param id Unique ID for the Member.
     * @return matching Member.
     */
    Member getMember(Integer id);

    /**
     * Retrieve Member instances by Email Address (Principal).
     * @param emailAddress String representation of what should be a valid email address.
     * @return Matching instance of Member.
     */
    Member getMemberByEmail(String emailAddress) throws AddressException;

    /**
     * Retrieve Member instances by Email Address (Principal).
     * @param emailAddress InternetAddress representation of the Principal's email address.
     * @return Matching instance of Member.
     */
    Member getMemberByEmail(InternetAddress emailAddress);

    /**
     * Creates a new Member record based on the ClueRideIdentity that comes from the Identity Provider.
     * @param clueRideIdentity personal data provided by Identity Provider.
     * @return Member instance built from ClueRideIdentity.
     */
    MemberEntity createNewMember(ClueRideIdentity clueRideIdentity);

    /**
     * Given a pattern, return a list of members that match the pattern.
     * @param pattern case-insensitive string to be matched.
     * @return List of matching {@link Member} instances.
     */
    List<Member> getMatchingMembers(String pattern);

    /**
     * Given a {@link Member} instance, check the details against what we have
     * as provided by the 3rd-party Auth service based on the Access Token.
     *
     * This is also an opportunity for these actions:
     * <ul>
     *     <li>Create/Update WordPress/BadgeOS account.</li>
     *     <li>Award Achievement for Registering the device.</li>
     * </ul>
     *
     * @param member to be checked.
     * @param authHeader the token which can be confirmed against 3rd-party Auth service.
     * @return Updates for conflicting information, if appropriate.
     */
    Member crossCheck(Member member, String authHeader);

}
