package com.clueride.domain.account.badgeos;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class BadgeOsUserServiceImpl implements BadgeOsUserService {
    @Inject
    private BadgeOsUserStore badgeOsUserStore;

    @Override
    public BadgeOsUserEntity add(BadgeOsUserEntity badgeOsUserEntity) {
        return badgeOsUserStore.add(badgeOsUserEntity);
    }

    @Nullable
    @Override
    public BadgeOsUserEntity getByEmailAddress(String emailAddress) {
        return badgeOsUserStore.getByEmailAddress(emailAddress);
    }

}
