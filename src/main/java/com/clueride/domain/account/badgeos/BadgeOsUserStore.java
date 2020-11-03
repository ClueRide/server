package com.clueride.domain.account.badgeos;

public interface BadgeOsUserStore {
    /**
     * Generate a new record for the given Badge OS User.
     *
     * @param badgeOsUserEntity instance to be created; ID is expected to be null.
     * @return same instance with the ID populated with the record from the DB.
     */
    BadgeOsUserEntity add(BadgeOsUserEntity badgeOsUserEntity);

    /**
     * Retrieve an existing BadgeOS user based on a match against the Email Address.
     *
     * @param emailAddress String representation of their Email Address.
     * @return Entity instance matching the given email Address or null if not found.
     */
    BadgeOsUserEntity getByEmailAddress(String emailAddress);
}
