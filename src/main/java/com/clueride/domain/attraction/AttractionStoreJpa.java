package com.clueride.domain.attraction;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class AttractionStoreJpa implements AttractionStore {

    @PersistenceContext(unitName="clueride")
    private EntityManager entityManager;

    @Override
    public AttractionEntity getById(Integer attractionId) {
        AttractionEntity entity = entityManager.find(AttractionEntity.class, attractionId);
        if (entity == null) {
            throw new AttractionNotFoundException("Attraction ID " + attractionId + " not found");
        }
        return entity;
    }

}