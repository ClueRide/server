package com.clueride.domain.course.link;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;

public class CourseToPathLinkStoreJpa implements CourseToPathLinkStore {
    @PersistenceContext(unitName = "clueride")
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    @Override
    public CourseToPathLinkEntity createNew(CourseToPathLinkEntity courseToPathLinkEntity) {
        try {
            userTransaction.begin();
            entityManager.persist(courseToPathLinkEntity);
            userTransaction.commit();
        } catch (NotSupportedException
                | RollbackException
                | HeuristicMixedException
                | HeuristicRollbackException
                | SystemException e
        ) {
            e.printStackTrace();
        }
        return courseToPathLinkEntity;
    }

    @Override
    public void remove(CourseToPathLinkEntity courseToPathLinkEntity) {
        try {
            userTransaction.begin();
            entityManager.createQuery("delete from CourseToPathLinkEntity where id = :id")
                    .setParameter("id", courseToPathLinkEntity.getId())
                    .executeUpdate();
            userTransaction.commit();
        } catch (
                NotSupportedException |
                        SystemException |
                        RollbackException |
                        HeuristicMixedException |
                        HeuristicRollbackException e
        ) {
            e.printStackTrace();
        }
    }

}
