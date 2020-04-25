package com.clueride.domain.attraction;

public interface AttractionStore {

    /**
     * Given an attractionId, return the corresponding AttractionEntity.
     *
     * @param attractionId unique identifier for the Attraction.
     * @return instance of AttractionEntity or throw AttractionNotFoundException
     * if not present.
     */
    AttractionEntity getById(Integer attractionId);

}
