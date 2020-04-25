package com.clueride.domain.flag;

import com.clueride.domain.attraction.AttractionNotFoundException;

import java.util.List;

/**
 * Defines operations on Flag instances.
 */
public interface FlagService {

    /**
     * Retrieves full list of all Flags against all Attractions.
     *
     * @return full list of all Flags.
     */
    List<Flag> getAllFlags();

    /**
     * Creates new instance of Flag against a specific Attraction.
     *
     * Throws AttractionNotFoundException if the Attraction doesn't exist in the DB.
     *
     * @param flag instance to be created against an Attraction.
     * @return The newly added Flag record.
     */
    Flag addNewFlag(FlagEntity flag) throws AttractionNotFoundException;

}
