package com.clueride.domain.account.badgeos;

import javax.inject.Inject;

public class BadgeOsUserServiceImpl implements BadgeOsUserService {
    @Inject
    private BadgeOsUserStore badgeOsUserStore;

    @Override
    public BadgeOsUserEntity add(BadgeOsUserEntity badgeOsUserEntity) {
        return badgeOsUserStore.add(badgeOsUserEntity);
    }

}
