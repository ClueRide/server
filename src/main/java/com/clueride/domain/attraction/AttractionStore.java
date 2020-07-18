package com.clueride.domain.attraction;

import java.util.List;

public interface AttractionStore {

    /**
     * Given an attractionId, return the corresponding AttractionEntity.
     *
     * @param attractionId unique identifier for the Attraction.
     * @return instance of AttractionEntity or throw AttractionNotFoundException
     * if not present.
     */
    AttractionEntity getById(Integer attractionId);

    /**
     * Retrieve all Attractions.
     *
     * Filtering for this particular call would happen client-side.
     *
     * @return List of all Attraction instances.
     */
    List<AttractionEntity> getAllAttractions();

    /**
     * Retrieve a list of up to 10 Attractions whose name matches the name Fragment passed in.
     *
     * @param nameFragment string representing a set of characters to match against.
     * @return List of matching Attractions.
     */
    List<AttractionEntity> getByNameFragment(String nameFragment);
}
