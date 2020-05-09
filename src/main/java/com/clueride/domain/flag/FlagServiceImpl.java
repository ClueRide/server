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
        List<Flag> flags = new ArrayList<>();
        for (FlagEntity flagEntity : flagStore.getFlags()) {
            flags.add(flagEntity.build());
        }
        return flags;
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
        LOGGER.info("Retrieving Flags for " + course.getName());
        LOGGER.debug("Attraction IDs: {}", course.getLocationIds());

        List<Flag> flags = new ArrayList<>();
        List<FlagEntity> flagEntities = flagStore.getFlagsForAttractions(
                courseService.getAttractionIdsForCourse(courseId)
        );

        for (FlagEntity flagEntity : flagEntities) {
            flags.add(flagEntity.build());
        }
        return flags;
    }

    @Override
    public List<FlagReason> getReasons() {
        return Arrays.asList(FlagReason.values());
    }

    private void validate(FlagEntity flagEntity) {
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
