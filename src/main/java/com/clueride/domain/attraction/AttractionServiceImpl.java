package com.clueride.domain.attraction;

import javax.inject.Inject;

/**
 * Implementation of {@link AttractionService} interface.
 */
public class AttractionServiceImpl implements AttractionService {

    private final AttractionStore attractionStore;

    @Inject
    public AttractionServiceImpl(
            AttractionStore attractionStore
    ) {
        this.attractionStore = attractionStore;
    }

    @Override
    public Attraction getById(Integer attractionId) {
        return attractionStore.getById(attractionId).build();
    }

}
