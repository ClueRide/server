package com.clueride.domain.path;

import com.clueride.domain.attraction.AttractionEntity;
import com.clueride.domain.attraction.AttractionStore;
import com.clueride.domain.course.Course;
import com.clueride.domain.course.CourseEntity;
import com.clueride.domain.course.CourseStore;
import com.clueride.domain.course.link.CourseToPathLinkEntity;
import com.clueride.domain.course.link.CourseToPathLinkStore;
import com.clueride.domain.path.meta.PathMeta;
import com.clueride.domain.path.meta.PathMetaEntity;
import com.clueride.domain.path.meta.PathMetaStore;
import com.clueride.network.path.PathStore;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathServiceImpl implements PathService {
    private CourseEntity courseEntity;
    private List<PathForCourseEntity> existingPathForCourseEntities;
    private Map<Integer, AttractionEntity> attractionMap = new HashMap<>();

    @Inject
    private CourseStore courseStore;

    @Inject
    private PathForCourseStore pathForCourseStore;

    @Inject
    private PathStore pathStore;

    @Inject
    private PathMetaStore pathMetaStore;

    @Inject
    private AttractionStore attractionStore;

    @Inject
    private CourseToPathLinkStore courseToPathLinkStore;

    @Override
    public Path getById(Integer id) {
        return null;
    }

    @Override
    public List<Path> getAll() {
        return null;
    }

    /**
     * This implementation tests existing Paths to see if we want to patch it up, or tear it down
     * and rebuild from scratch. The easy approach is to add to existing if we don't find enough
     * and teardown and rebuild if we have too many.
     *
     * @param courseId Unique identifier to for the {@link Course}.
     * @param newAttractionIds requested ordered List of Attraction IDs to link together.
     * @return updated list of {@link PathMeta} instances.
     */
    @Override
    public List<PathMeta> getLinkPathsForAttractions(
            Integer courseId,
            List<Integer> newAttractionIds
    ) {
        List<PathMeta> paths = new ArrayList<>();
        List<CourseToPathLinkEntity> courseToPathEntities = new ArrayList<>();

        if (newAttractionIds.size() < 2) {
            /* No reason to prepare link paths for less than a single pair of newAttractionIds. */
            return paths;
        }

        /* Store a local copy of what we have at this point before the update. */
        existingPathForCourseEntities = pathForCourseStore.getPathsForCourse(courseId);

        /* We'll want a current instance from the DB. */
        courseEntity = courseStore.getCourseById(courseId);

        /* Loop through the list of requested Attaction IDs. */
        for (Integer attractionId : newAttractionIds) {
            attractionMap.put(attractionId, attractionStore.getById(attractionId));
        }

        /* Run through pairs of Attraction IDs determining which have existing Paths for the course. */
        for (int i = 0; i < newAttractionIds.size() - 1; i++) {
            /* Obtain appropriate Path instance. */
            PathMeta path = getPathForAttractionPair(
                    i+1,
                    newAttractionIds.get(i),
                    newAttractionIds.get(i+1)
            );
            paths.add(path);
        }

        removeObsoleteCourseToPathRecords(
                newAttractionIds,
                existingPathForCourseEntities
        );

        /* Make and persist changes to the Course. */
        courseEntity.withCourseToPathEntities(courseToPathEntities);
        courseStore.update(courseEntity);

        return paths;
    }

    /**
     * For the given pair of Attraction IDs, look for an existing Path, and if not found,
     * create a new Path with an empty set of Edges.
     *
     * At the same time, we're building a list of CourseToPathEntities that we no longer need; the Paths however,
     * may be useful in the future, particularly if they have edges.
     *
     * @param startId Attraction ID for the start (or end, really) of this Path.
     * @param endId Attraction ID for the end (or start, really) of this Path.
     * @return either existing Path (with or without Edges) or a new Path (without Edges).
     */
    @Nonnull
    private PathMeta getPathForAttractionPair(
            Integer indexWithinCourse,
            Integer startId,
            Integer endId
    ) {
        PathMetaEntity pathMetaEntity;

        PathForCourseEntity existingPath = findExistingPath(startId, endId);
        if (existingPath != null) {
            /* Create from existing. */
            pathMetaEntity = PathForCourseEntity.from(existingPath)
                    .withHasEdges(
                            pathStore.getEdgeCount(existingPath.getPathId()) > 0
                    );
            /* If path exists, no need to mess with the CourseToPath instances. */
        } else {
            /* Instantiate a new one. */
            pathMetaEntity = PathMetaEntity.builder()
                    .withStartAttractionId(startId)
                    .withStartNodeId(attractionMap.get(startId).getNodeId())
                    .withEndAttractionId(endId)
                    .withEndNodeId(attractionMap.get(endId).getNodeId())
                    .withHasEdges(false);

            // Persist it so it exists in the DB.
            pathMetaStore.createNew(pathMetaEntity);

            /* Obtain appropriate link between Path and Course with incrementing order. */
            CourseToPathLinkEntity courseToPathLinkEntity = CourseToPathLinkEntity.builder()
                    .withPathOrder(indexWithinCourse)
                    .withPathId(pathMetaEntity.getId())
                    .withCourse(courseEntity);

            courseToPathLinkStore.createNew(courseToPathLinkEntity);

            courseEntity.withCourseToPathEntity(
                    courseToPathLinkEntity
            );
        }

        return pathMetaEntity.build();
    }

    @Nullable
    private PathForCourseEntity findExistingPath(
            Integer startId,
            Integer endId
    ) {
        for (PathForCourseEntity pathForCourseEntity : existingPathForCourseEntities) {
            if (pathForCourseEntity.getStartLocationId().equals(startId) &&
                    pathForCourseEntity.getEndLocationId().equals(endId)
            ) {
                return pathForCourseEntity;
            }
        }
        return null;
    }

    /**
     * Cycles through each of the existing Path For Course Entities to see which ones are no longer needed.
     *
     * @param newAttractionIds The desired ordered list of Attraction IDs.
     * @param existingPathForCourseEntities what we used to have for the PathForCourseEntity records.
     */
    private void removeObsoleteCourseToPathRecords(
            List<Integer> newAttractionIds,
            List<PathForCourseEntity> existingPathForCourseEntities
    ) {

        for (PathForCourseEntity pathForCourseEntity : existingPathForCourseEntities) {
            /* brute force */
            boolean removePath = true;
            for (int index = 0; index < newAttractionIds.size()-1; index++) {
                if (pathForCourseEntity.getStartLocationId().equals(newAttractionIds.get(index))
                    && pathForCourseEntity.getEndLocationId().equals(newAttractionIds.get(index+1))) {
                    removePath = false;
                    break;
                }
            }

            if (removePath) {
                CourseToPathLinkEntity courseToPathLinkEntity = CourseToPathLinkEntity.builder()
                        .withId(pathForCourseEntity.getId());

                courseToPathLinkStore.remove(courseToPathLinkEntity);
            }
        }
    }

}
