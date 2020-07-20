package com.clueride.domain.path;

import com.clueride.domain.attraction.AttractionEntity;
import com.clueride.domain.attraction.AttractionStore;
import com.clueride.domain.course.CourseEntity;
import com.clueride.domain.course.CourseStore;
import com.clueride.domain.course.CourseToPathEntity;
import com.clueride.domain.course.CourseToPathStore;
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
    private List<PathForCourseEntity> pathForCourseEntities;
    private Map<Integer, AttractionEntity> attractionMap = new HashMap<>();

    @Inject
    private CourseStore courseStore;

    @Inject
    private PathForCourseStore pathForCourseStore;

    @Inject
    private PathStore pathStore;

    @Inject
    private LinkPathStore linkPathStore;

    @Inject
    private AttractionStore attractionStore;

    @Inject
    private CourseToPathStore courseToPathStore;

    @Override
    public Path getById(Integer id) {
        return null;
    }

    @Override
    public List<Path> getAll() {
        return null;
    }

    @Override
    public List<LinkPath> getLinkPathsForAttractions(Integer courseId, List<Integer> attractionIds) {
        List<LinkPath> paths = new ArrayList<>();
        List<CourseToPathEntity> courseToPathEntities = new ArrayList<>();

        /* Nothing to be done if we don't have at least one pair. */
        if (attractionIds.size() < 2) {
            return paths;
        }

        for (Integer attractionId : attractionIds) {
            attractionMap.put(attractionId, attractionStore.getById(attractionId));
        }
        courseEntity = courseStore.getCourseById(courseId);
        pathForCourseEntities = pathForCourseStore.getPathsForCourse(courseId);

        /* Run through pairs of Attraction IDs determining which have existing Paths for the course. */
        for (int i = 0; i < attractionIds.size() - 1; i++) {
            /* Obtain appropriate Path instance. */
            LinkPath path = getPathForAttractionPair(
                    i+1,
                    attractionIds.get(i),
                    attractionIds.get(i+1)
            );
            paths.add(path);
        }

        /* Make and persist changes to the Course. */
        courseEntity.withCourseToPathEntities(courseToPathEntities);
        courseStore.update(courseEntity);

        return paths;
    }

    /**
     * For the given pair of Attraction IDs, look for an existing Path on the course, and if not found,
     * create a new Path with an empty set of Edges.
     *
     * @param startId Attraction ID for the start (or end, really) of this Path.
     * @param endId Attraction ID for the end (or start, really) of this Path.
     * @return either existing Path (with or without Edges) or a new Path (without Edges).
     */
    @Nonnull
    private LinkPath getPathForAttractionPair(
            Integer indexWithinCourse,
            Integer startId,
            Integer endId
    ) {
        LinkPathEntity linkPathEntity;

        PathForCourseEntity existingPath = findExistingPath(startId);
        if (existingPath != null) {
            /* Create from existing. */
            linkPathEntity = PathForCourseEntity.from(existingPath)
                    .withHasEdges(
                            pathStore.getEdgeCount(existingPath.getPathId()) > 0
                    );
            /* If path exists, no need to mess with the CourseToPath instances. */
        } else {
            /* Instantiate a new one. */
            linkPathEntity = LinkPathEntity.builder()
                    .withStartAttractionId(startId)
                    .withStartNodeId(attractionMap.get(startId).getNodeId())
                    .withEndAttractionId(endId)
                    .withEndNodeId(attractionMap.get(endId).getNodeId())
                    .withHasEdges(false);

            // Persist it so it exists in the DB.
            linkPathStore.createNew(linkPathEntity);

            /* Obtain appropriate link between Path and Course with incrementing order. */
            CourseToPathEntity courseToPathEntity = CourseToPathEntity.builder()
                    .withPathOrder(indexWithinCourse)
                    .withPathId(linkPathEntity.getId())
                    .withCourse(courseEntity);

            courseToPathStore.createNew(courseToPathEntity);

            courseEntity.withCourseToPathEntity(
                    courseToPathEntity
            );
        }

        return linkPathEntity.build();
    }

    @Nullable
    private PathForCourseEntity findExistingPath(Integer startId) {
        for (PathForCourseEntity pathForCourseEntity : pathForCourseEntities) {
            if (pathForCourseEntity.getStartLocationId().equals(startId)) {
                return pathForCourseEntity;
            }
        }
        return null;
    }

}
