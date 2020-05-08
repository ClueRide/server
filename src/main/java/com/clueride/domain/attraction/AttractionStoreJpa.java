package com.clueride.domain.attraction;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class AttractionStoreJpa implements AttractionStore {

    @PersistenceContext(unitName="clueride")
    private EntityManager entityManager;

    @Override
    public AttractionEntity getById(Integer attractionId) {
        return entityManager.find(AttractionEntity.class, attractionId);
    }

}
