package com.clueride.domain.path.meta;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;

public class PathMetaStoreJpa implements PathMetaStore {

    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Override
    public PathMetaEntity createNew(PathMetaEntity pathMetaEntity) {
        try {
            userTransaction.begin();
            entityManager.persist(pathMetaEntity);
            userTransaction.commit();
        } catch (NotSupportedException
                | SystemException
                | RollbackException
                | HeuristicMixedException
                | HeuristicRollbackException e
        ) {
            e.printStackTrace();
        }
        return pathMetaEntity;
    }

}
