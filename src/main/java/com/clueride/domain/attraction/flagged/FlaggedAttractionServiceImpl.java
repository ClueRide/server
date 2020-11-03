package com.clueride.domain.attraction.flagged;

import com.clueride.domain.attraction.Attraction;
import com.clueride.domain.attraction.AttractionEntity;
import com.clueride.domain.attraction.AttractionStore;
import com.clueride.domain.flag.FlagService;
import com.clueride.domain.location.latlon.LatLonService;
import com.clueride.domain.place.ScoredLocationService;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class FlaggedAttractionServiceImpl implements FlaggedAttractionService {
    @Inject
    private Logger LOGGER;

    private final AttractionStore attractionStore;
    private final FlagService flagService;
    private final LatLonService latLonService;
    private final ScoredLocationService scoredLocationService;

    @Inject
    public FlaggedAttractionServiceImpl(
            AttractionStore attractionStore,
            FlagService flagService,
            LatLonService latLonService,
            ScoredLocationService scoredLocationService
    ) {
        this.attractionStore = attractionStore;
        this.flagService = flagService;
        this.latLonService = latLonService;
        this.scoredLocationService = scoredLocationService;
    }

    @Override
    public Attraction getByIdWithFlags(Integer attractionId) {
        AttractionEntity attractionEntity = attractionStore.getById(attractionId)
                .withFlags(flagService.getFlagsForAttraction(attractionId));

        return attractionEntity.build();
    }

    @Override
    public List<Attraction> getAllAttractions() {
        LOGGER.info("Retrieving full set of Attractions with Flags");
        List<Attraction> attractions = new ArrayList<>();
        for (AttractionEntity entity : attractionStore.getAllAttractions()) {
            fillAndGradeAttraction(entity);
            attractions.add(entity.build());
        }
        return attractions;
    }

    /**
     * Adds in the following transient fields:
     * <OL>
     *     <li>LatLon</li>
     *     <li>Flags</li>
     *     <li>ReadinessLevel</li>
     * </OL>
     *
     * @param entity
     */
    private void fillAndGradeAttraction(AttractionEntity entity) {
        entity.withLatLon(latLonService.getLatLonById(entity.getNodeId()));
        entity.withFlags(flagService.getFlagsForAttraction(entity.getId()));
        entity.withReadinessLevel(scoredLocationService.calculateReadinessLevel(entity));
    }

}
