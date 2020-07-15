package com.clueride.domain.flag;

import com.clueride.domain.attraction.Attraction;
import com.clueride.domain.attraction.AttractionNotFoundException;
import com.clueride.domain.attraction.AttractionService;
import com.clueride.domain.course.Course;
import com.clueride.domain.course.CourseService;
import com.clueride.domain.flag.reason.FlagReason;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class FlagServiceImpl implements FlagService {
    @Inject
    private Logger LOGGER;

    private final FlagStore flagStore;
    private final AttractionService attractionService;
    private final CourseService courseService;

    @Inject
    public FlagServiceImpl(
            FlagStore flagStore,
            AttractionService attractionService,
            CourseService courseService
    ) {
        this.flagStore = flagStore;
        this.attractionService = attractionService;
        this.courseService = courseService;
    }

    @Override
    public List<Flag> getAllFlags() {
        return buildFlagEntities(
                flagStore.getFlags()
        );
    }

    @Override
    public Flag addNewFlag(FlagEntity flagEntity) throws AttractionNotFoundException {
        validate(flagEntity);

        flagStore.addNew(flagEntity);
        return flagEntity.build();
    }

    @Override
    public List<Flag> getFlagsForCourse(Integer courseId) {
        /* Check existence of Course; exception is thrown if not found. */
        Course course = courseService.getById(courseId);
        LOGGER.debug("Retrieving Flags for " + course.getName());
        LOGGER.debug("Attraction IDs: {}", course.getLocationIds());

        List<FlagEntity> flagEntities = flagStore.getFlagsForAttractions(
                courseService.getAttractionIdsForCourse(courseId)
        );

        return buildFlagEntities(flagEntities);
    }

    @Override
    public List<Flag> getFlagsForAttraction(Integer attractionId) {
        List<FlagEntity> flagEntities = flagStore.getFlagsForAttractions(Collections.singletonList(attractionId));
        if (flagEntities.size() > 0 ) {
            LOGGER.info("Retrieving Flags for Attraction ID {}", attractionId);
        }
        return buildFlagEntities(flagEntities);
    }

    /**
     * Given a List of Flag Entities, runs `build` on each to return List of Flag.
     *
     * @param flagEntities List of mutable Flag Entities.
     * @return List of immutable {@link Flag}.
     */
    private List<Flag> buildFlagEntities(List<FlagEntity> flagEntities) {
        List<Flag> flags = new ArrayList<>();
        for (FlagEntity flagEntity : flagEntities) {
            flags.add(flagEntity.build());
        }
        return flags;
    }

    @Override
    public List<FlagReason> getReasons() {
        return Arrays.asList(FlagReason.values());
    }

    @Override
    public List<FlaggedAttribute> getAttributes() {
        return Arrays.asList(FlaggedAttribute.values());
    }

    private void validate(FlagEntity flagEntity) {
        LOGGER.info("Validating Flag against Attraction ID {}", flagEntity.getAttractionId());

        requireNonNull(
                flagEntity.getReason(),
                "Flag Reason cannot be null"
        );

        Integer attractionId = requireNonNull(flagEntity.getAttractionId(), "Attraction ID must be provided");
        Attraction attraction = attractionService.getById(attractionId);
        if (attraction == null) {
            throw new AttractionNotFoundException("Attraction ID " + attractionId + " not found");
        }

    }


}
