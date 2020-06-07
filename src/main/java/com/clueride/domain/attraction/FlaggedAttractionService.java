package com.clueride.domain.attraction;

import java.util.List;

public interface FlaggedAttractionService {

    /**
     * Given an Attraction ID, retrieve the Attraction and populate any Issue Flags
     * the attraction may have.
     *
     * @param attractionId unique identifier for the {@link Attraction}.
     * @return Attraction matching the Attraction ID; throws
     * AttractionNotFoundException if no matching AttractionID.
     */
    Attraction getByIdWithFlags(Integer attractionId);

    /**
     * Retrieve all Attractions and populate with any issues those attractions
     * may have.
     *
     * @return List of all Attractions with their Flags populated.
     */
    List<Attraction> getAllAttractions();

}
