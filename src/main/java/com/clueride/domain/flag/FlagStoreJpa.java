package com.clueride.domain.flag;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;
import java.util.List;

public class FlagStoreJpa implements FlagStore {

    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Override
    public Integer addNew(FlagEntity flagEntity) {
        try {
                userTransaction.begin();
                entityManager.persist(flagEntity);
                userTransaction.commit();
        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException e) {
            e.printStackTrace();
        }
        return flagEntity.getId();
    }

    @Override
    public List<FlagEntity> getFlags() {
        return entityManager.createQuery(
                "SELECT f FROM FlagEntity f",
                FlagEntity.class
        ).getResultList();
    }

    @Override
    public FlagEntity update(FlagEntity flagEntity) {
        return null;
    }

    @Override
    public List<FlagEntity> getFlagsForAttractions(List<Integer> attractionIds) {
        return entityManager.createQuery(
                "SELECT f from FlagEntity f WHERE attractionId in :ids",
                FlagEntity.class
        ).setParameter("ids", attractionIds)
                .getResultList();
    }

}
