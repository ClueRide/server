package com.clueride.domain.account.badgeos;

public interface BadgeOsUserService {

    /**
     * Generate a new record for the given Badge OS User.
     *
     * @param badgeOsUserEntity instance to be created; ID is expected to be null.
     * @return same instance with the ID populated with the record from the DB.
     */
    BadgeOsUserEntity add(BadgeOsUserEntity badgeOsUserEntity);

}
