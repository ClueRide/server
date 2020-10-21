package com.clueride.domain.path.edge;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;

public class PathEdgeStoreJpa implements PathEdgeStore {
    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Override
    public PathEdgeEntity createNew(PathEdgeEntity pathEdgeEntity) {
        try {
            userTransaction.begin();
            entityManager.persist(pathEdgeEntity);
            userTransaction.commit();
        } catch (NotSupportedException
                | SystemException
                | RollbackException
                | HeuristicMixedException
                | HeuristicRollbackException e
        ) {
            e.printStackTrace();
        }
        return pathEdgeEntity;
    }
}
