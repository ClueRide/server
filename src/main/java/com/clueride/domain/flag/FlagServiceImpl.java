package com.clueride.domain.flag;

import com.clueride.domain.attraction.Attraction;
import com.clueride.domain.attraction.AttractionNotFoundException;
import com.clueride.domain.attraction.AttractionService;
import com.clueride.domain.flag.reason.FlagReason;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class FlagServiceImpl implements FlagService {
    private final FlagStore flagStore;
    private final AttractionService attractionService;

    @Inject
    public FlagServiceImpl(
            FlagStore flagStore,
            AttractionService attractionService
    ) {
        this.flagStore = flagStore;
        this.attractionService = attractionService;
    }

    @Override
    public List<Flag> getAllFlags() {
        List<Flag> flags = new ArrayList<>();
        for (FlagEntity flagEntity : flagStore.getFlags()) {
            flags.add(flagEntity.build());
        }
        return flags;
    }

    @Override
    public Flag addNewFlag(FlagEntity flagEntity) throws AttractionNotFoundException {
        Integer attractionId = requireNonNull(flagEntity.getAttractionId(), "Attraction ID must be provided");
        Attraction attraction = attractionService.getById(attractionId);
        if (attraction == null) {
            throw new AttractionNotFoundException("Attraction ID " + attractionId + " not found");
        }

        validate(flagEntity);

        flagStore.addNew(flagEntity);
        return flagEntity.build();
    }

    private void validate(FlagEntity flagEntity) {
//        FlaggedAttribute flaggedAttribute = requireNonNull(
//                flagEntity.getFlaggedAttribute(),
//                "Flagged Attribute cannot be null"
//        );

        FlagReason reason = requireNonNull(
                flagEntity.getReason(),
                "Flag Reason cannot be null"
        );
    }


}
