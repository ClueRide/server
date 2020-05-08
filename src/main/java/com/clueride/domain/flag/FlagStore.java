package com.clueride.domain.flag;

import java.util.List;

public interface FlagStore {
    /**
     * Retrieves list of all Flags across all Attractions.
     *
     * @return Full List of {@link FlagEntity}.
     */
    List<FlagEntity> getFlags();

    /**
     * Persists a new instance of FlagEntity against a specific Attraction.
     *
     * @param flagEntity instance to be created.
     * @return Unique identifier assigned by the database.
     */
    Integer addNew(FlagEntity flagEntity);

    /**
     * Update an existing FlagEntity with the data in this instance.
     *
     * @param flagEntity instance to update containing new attributes.
     * @return the updated instance.
     */
    FlagEntity update(FlagEntity flagEntity);

}
