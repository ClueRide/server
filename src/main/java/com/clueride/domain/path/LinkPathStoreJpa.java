package com.clueride.domain.path;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;

public class LinkPathStoreJpa implements LinkPathStore {

    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Override
    public LinkPathEntity createNew(LinkPathEntity linkPathEntity) {
        try {
            userTransaction.begin();
            entityManager.persist(linkPathEntity);
            userTransaction.commit();
        } catch (NotSupportedException
                | SystemException
                | RollbackException
                | HeuristicMixedException
                | HeuristicRollbackException e
        ) {
            e.printStackTrace();
        }
        return linkPathEntity;
    }

}
