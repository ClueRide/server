package com.clueride.domain.attraction;

import com.clueride.domain.flag.FlagService;

import javax.inject.Inject;

public class FlaggedAttractionServiceImpl implements FlaggedAttractionService {

    private final AttractionStore attractionStore;
    private final FlagService flagService;

    @Inject
    public FlaggedAttractionServiceImpl(
            AttractionStore attractionStore,
            FlagService flagService
    ) {
        this.attractionStore = attractionStore;
        this.flagService = flagService;
    }

    @Override
    public Attraction getByIdWithFlags(Integer attractionId) {
        AttractionEntity attractionEntity = attractionStore.getById(attractionId)
                .withFlags(flagService.getFlagsForAttraction(attractionId));

        return attractionEntity.build();
    }

}
