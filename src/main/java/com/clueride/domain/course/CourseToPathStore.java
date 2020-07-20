package com.clueride.domain.course;

/**
 * Persistence layer for the link between a Path and its Course.
 */
public interface CourseToPathStore {
    /**
     * Adds new record for a {@link CourseToPathEntity} instance.
     * @param courseToPathEntity instance to be persisted; no ID record.
     * @return {@link CourseToPathEntity} instance with ID populated from DB.
     */
    CourseToPathEntity createNew(CourseToPathEntity courseToPathEntity);
}
