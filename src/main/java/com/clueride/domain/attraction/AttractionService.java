package com.clueride.domain.attraction;

import java.util.List;

public interface AttractionService {
    /**
     * Given an Attraction ID, retrieve the Attraction.
     *
     * @param attractionId unique identifier for the {@link Attraction}.
     * @return Attraction matching the Attraction ID; throws
     * AttractionNotFoundException if no matching AttractionID.
     */
    Attraction getById(Integer attractionId);

    /**
     * Auto-suggest support; returns list of Matching Attractions by name.
     *
     * @param nameFragmentQuery holds the name fragment we're matching against.
     * @return List of matching names, limited to the first 10 matches.
     */
    List<Attraction> getByNameFragment(NameFragmentQuery nameFragmentQuery);

    /**
     * Given the Course ID, return the ordered list of Attractions for the Course.
     *
     * @param courseId unique identifier for the Course.
     * @return Ordered list of the Attractions for the Course.
     */
    List<Attraction> getAttractionsForCourse(Integer courseId);
}
