package com.clueride.domain.flag;

import com.clueride.domain.attraction.AttractionNotFoundException;
import com.clueride.domain.course.Course;
import com.clueride.domain.flag.reason.FlagReason;

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

    /**
     * Given a Course ID, find the list of Flagged items for this Course.
     *
     * @param courseId unique identifier for the {@link Course}.
     * @return List of the active and inactive flags for the given Course; may be an empty list.
     */
    List<Flag> getFlagsForCourse(Integer courseId);

    /**
     * Supports front-end by providing a list of the valid Flag Reasons.
     *
     * @return List of {@link FlagReason}.
     */
    List<FlagReason> getReasons();

}
