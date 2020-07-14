package com.clueride.domain.account.badgeos;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;

public class BadgeOsUserStoreJpa implements BadgeOsUserStore {

    @PersistenceContext(unitName = "badgeOS")
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Override
    public BadgeOsUserEntity add(BadgeOsUserEntity badgeOsUserEntity) {
        try {
            userTransaction.begin();
            entityManager.persist(badgeOsUserEntity);
            userTransaction.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
            e.printStackTrace();
        }
        return badgeOsUserEntity;
    }

}
