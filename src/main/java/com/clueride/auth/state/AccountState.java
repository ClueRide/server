package com.clueride.auth.state;

import com.clueride.domain.account.member.Member;

/**
 * AccountState summarizes where an Account sits in its life-cycle.
 *
 * <ul>
 *     <li>EXISTING represents the stable existence of an Account with records. It may need
 *     to renew its access token, but the account itself is well-established with records in both
 *     this server ({@link Member} records) and BadgeOS.</li>
 *
 *     <li>NEW represents an account that has been verified by the 3rd-party Auth tool, and has a
 *     set of profile data that can be used to prepare both Member and BadgeOS records.</li>
 *
 *     <li>TEST represents either an account with the configured Bearer token, or an account
 *     recognized as a ClueRide Test account. It is given an access token that does not require
 *     3rd-party validation and can only run on the test instances.</li>
 *
 *     <li>INVALID represents an account that cannot be validated for one of several reasons.</li>
 *
 *     <li>UNRECOGNIZED represents a token that can't be turned into an account for us to check;
 *     3rd-party isn't able match token with a principal.</li>
 *
 *     <li>ARCHIVED represents the historical records of an inactive user whose records are kept intact
 *     for the purposes of documenting badge awards.</li>
 *
 *     NOTE: A user's account that is deleted does not have a state in this system. Since all records are
 *     deleted, if the user comes back, they are given a NEW user state.
 * </ul>
 */
public enum AccountState {
    EXISTING,
    NEW,
    TEST,
    INVALID,
    UNRECOGNIZED,
    ARCHIVED
}
