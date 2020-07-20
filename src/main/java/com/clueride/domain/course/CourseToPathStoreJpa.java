package com.clueride.domain.course;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;

public class CourseToPathStoreJpa implements CourseToPathStore {
    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Override
    public CourseToPathEntity createNew(CourseToPathEntity courseToPathEntity) {
        try {
            userTransaction.begin();
            entityManager.persist(courseToPathEntity);
            userTransaction.commit();
        } catch (NotSupportedException
                | RollbackException
                | HeuristicMixedException
                | HeuristicRollbackException
                | SystemException e
        ) {
            e.printStackTrace();
        }
        return courseToPathEntity;
    }
}
