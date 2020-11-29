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
    public PathMetaEntity get(Integer pathId) {
        return entityManager.find(PathMetaEntity.class, pathId);
    }

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

    @Override
    public PathMetaEntity findSuitablePath(Integer startNodeId, Integer endNodeId) {
        return entityManager.createQuery("SELECT p from PathMetaEntity p " +
                "where startNodeId = :startNodeId " +
                "  and endNodeId = :endNodeId", PathMetaEntity.class)
                .setParameter("startNodeId", startNodeId)
                .setParameter("endNodeId", endNodeId)
                .getSingleResult();
    }

}
