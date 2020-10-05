package com.clueride.domain.course.link;

/**
 * Persistence layer for the link between a Path and its Course.
 */
public interface CourseToPathLinkStore {
    /**
     * Adds new record for a {@link CourseToPathLinkEntity} instance.
     * @param courseToPathLinkEntity instance to be persisted; no ID record.
     * @return {@link CourseToPathLinkEntity} instance with ID populated from DB.
     */
    CourseToPathLinkEntity createNew(CourseToPathLinkEntity courseToPathLinkEntity);

    /**
     * Breaks the link between a Course and a particular Path.
     *
     * The Path itself remains in case another Course uses it.
     *
     * @param courseToPathLinkEntity the link to break.
     */
    void remove(CourseToPathLinkEntity courseToPathLinkEntity);

}
