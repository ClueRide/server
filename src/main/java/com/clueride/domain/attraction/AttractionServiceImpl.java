package com.clueride.domain.attraction;

import com.clueride.domain.attraction.flagged.FlaggedAttractionService;
import com.clueride.domain.course.CourseService;
import com.clueride.domain.location.loctype.LocationType;
import com.clueride.domain.location.loctype.LocationTypeService;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link AttractionService} interface.
 */
public class AttractionServiceImpl implements AttractionService {
    @Inject
    private Logger LOGGER;

    private final AttractionStore attractionStore;
    private final CourseService courseService;
    private final FlaggedAttractionService flaggedAttractionService;
    private final LocationTypeService locationTypeService;

    @Inject
    public AttractionServiceImpl(
            AttractionStore attractionStore,
            CourseService courseService,
            FlaggedAttractionService flaggedAttractionService,
            LocationTypeService locationTypeService
    ) {
        this.attractionStore = attractionStore;
        this.courseService = courseService;
        this.flaggedAttractionService = flaggedAttractionService;
        this.locationTypeService = locationTypeService;
    }

    @Override
    public Attraction getById(Integer attractionId) {
        return attractionStore.getById(attractionId).build();
    }

    @Override
    public List<Attraction> getByNameFragment(NameFragmentQuery nameFragmentQuery) {
        List<Attraction> attractions = new ArrayList<>();
        for (AttractionEntity entity : attractionStore.getByNameFragment(nameFragmentQuery.fragment)) {
            flaggedAttractionService.fillAndGradeAttraction(entity);
            attractions.add(entity.build());
        }
        return attractions;
    }

    @Override
    public List<Attraction> getAttractionsForCourse(Integer courseId) {
        LOGGER.debug("Retrieving list of Attractions for Course {}", courseId);
        List<Attraction> attractions = new ArrayList<>();
        List<Integer> attractionIds = courseService.getAttractionIdsForCourse(courseId);
        for (Integer attractionId : attractionIds) {
            AttractionEntity attractionEntity = attractionStore.getById(attractionId);
            flaggedAttractionService.fillAndGradeAttraction(attractionEntity);
            fillLocationType(attractionEntity);
            attractions.add(attractionEntity.build());
        }
        return attractions;
    }

    /**
     * Populates the Transient Location Type values relevant to displaying Icons on the client.
     * @param attractionEntity
     */
    private void fillLocationType(AttractionEntity attractionEntity) {
        LocationType locationType = locationTypeService.getById(attractionEntity.getLocationTypeId());
        attractionEntity.withLocationTypeName(locationType.getName());
        attractionEntity.withLocationTypeIcon(locationType.getIcon());
    }
}
