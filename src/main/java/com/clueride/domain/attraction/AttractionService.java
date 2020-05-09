package com.clueride.domain.attraction;

public interface AttractionService {
    /**
     * Given an Attraction ID, retrieve the Attraction.
     *
     * @param attractionId unique identifier for the {@link Attraction}.
     * @return Attraction matching the Attraction ID; throws
     * AttractionNotFoundException if no matching AttractionID.
     */
    Attraction getById(Integer attractionId);
}
