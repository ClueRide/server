package com.clueride.domain.path;

import com.clueride.domain.attraction.AttractionEntity;
import com.clueride.domain.attraction.AttractionStore;
import com.clueride.domain.course.Course;
import com.clueride.domain.course.CourseEntity;
import com.clueride.domain.course.link.CourseToPathLinkEntity;
import com.clueride.domain.course.link.CourseToPathLinkStore;
import com.clueride.domain.path.attractions.CoursePathAttractionsEntity;
import com.clueride.domain.path.attractions.CoursePathAttractionsStore;
import com.clueride.domain.path.meta.PathMeta;
import com.clueride.domain.path.meta.PathMetaEntity;
import com.clueride.domain.path.meta.PathMetaStore;
import com.clueride.network.path.PathStore;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import java.util.*;

import static java.util.Objects.requireNonNull;

public class PathServiceImpl implements PathService {
    private Map<Integer, AttractionEntity> attractionMap = new HashMap<>();

    @Inject
    private CoursePathAttractionsStore coursePathAttractionsStore;

    @Inject
    private Logger LOGGER;

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

    @Override
    public List<Integer> getLocationIds(Integer courseId) {
        requireNonNull(courseId, "Course ID must be provided");

        List<CoursePathAttractionsEntity> paths = coursePathAttractionsStore.getPathAttractionsForCourse(courseId);
        if (paths.size() == 0) {
            LOGGER.warn("No paths found for course ID {}", courseId );
            return Collections.emptyList();
        }

        List<Integer> locationIds = new ArrayList<>();
        CoursePathAttractionsEntity lastBuilder = null;
        for (CoursePathAttractionsEntity builder : paths) {
            locationIds.add(builder.getStartLocationId());
            lastBuilder = builder;
        }
        locationIds.add(lastBuilder.getEndLocationId());

        return locationIds;
    }

    /**
     * This implementation tests existing Paths to see if we want to patch it up, or tear it down
     * and rebuild from scratch. The easy approach is to add to existing if we don't find enough
     * and teardown and rebuild if we have too many.
     *
     * We want to create Path records for potential combinations of Attractions, but it isn't until
     * we persist the Course that we worry about linking the course to those Paths.
     *
     * @param courseId Unique identifier to for the {@link Course}.
     * @param newAttractionIds requested ordered List of Attraction IDs to link together.
     * @return updated list of {@link PathMeta} instances.
     */
    @Override
    public List<PathMeta> getPathMetaForAttractions(
            Integer courseId,
            List<Integer> newAttractionIds
    ) {
        List<PathMeta> paths = new ArrayList<>();

        if (newAttractionIds.size() < 2) {
            LOGGER.warn("Less than 2 Attractions; no path to be setup yet");
            /* No reason to prepare link paths for less than a single pair of newAttractionIds. */
            return paths;
        }
        LOGGER.debug("Setting up Paths for {} Attractions",
                newAttractionIds.size()
        );

        /* Loop through the list of requested Attraction IDs. */
        for (Integer attractionId : newAttractionIds) {
            attractionMap.put(attractionId, attractionStore.getById(attractionId));
        }

        /* Run through pairs of Attraction IDs determining which have existing Paths for the course. */
        for (int i = 0; i < newAttractionIds.size() - 1; i++) {
            int start_id = newAttractionIds.get(i);
            int end_id = newAttractionIds.get(i+1);

            /* Obtain appropriate Path instance. */
            PathMeta path = getPathForAttractionPair(
                    attractionMap.get(start_id),
                    attractionMap.get(end_id)
            );
            paths.add(path);
        }

        return paths;
    }

    /**
     * Generates set of Course to Path Links to be persisted with the Course starting
     * with the ordered list of Attraction IDs.
     *
     * All required Path records for each pair of Attractions are expected to have been
     * persisted already, but the {@link #getPathMetaForAttractions(Integer, List)} assures
     * this is complete.
     *
     * An earlier implementation attempted to re-use Link records (course_to_path), but
     * after adding a DB constraint to prevent duplicates (path_order for a course ID),
     * it became too much trouble to try to re-apply a given link. Now we just tear down
     * all the link records and provide a complete new set.
     *
     * @param courseEntity course possessing the list of Attractions.
     * @return full set of links between the Course and its Paths.
     */
    @Override
    public List<CourseToPathLinkEntity> getCourseToPathLinkEntities(CourseEntity courseEntity) {
        List<PathMeta> pathMetaList = getPathMetaForAttractions(
                courseEntity.getId(),
                courseEntity.getLocationIds()
        );

        List<CourseToPathLinkEntity> newCourseToPathLinkEntities = new ArrayList<>();
        /* If not enough attractions for a Path, we won't record any Paths. */
        if (pathMetaList.size() == 0) {
            return newCourseToPathLinkEntities;
        }

        /* Since we're building a new set, clear all the old links. */
        courseToPathLinkStore.dropAllForCourse(courseEntity.getId());

        /* Build new set. */
        int pathOrder = 1;
        for (PathMeta pathMeta : pathMetaList) {
            newCourseToPathLinkEntities.add(
                    CourseToPathLinkEntity.builder()
                            .withCourse(courseEntity)
                            .withPathId(pathMeta.getId())
                            .withPathOrder(pathOrder++)
            );
        }
        return newCourseToPathLinkEntities;
    }

    /**
     * For the given pair of Attractions, look for an existing Path, and if not found,
     * create a new Path with an empty set of Edges.
     *
     * @param startAttraction attraction for the start of this Path.
     * @param endAttraction attraction for the end of this Path.
     * @return either existing Path (with or without Edges) or a new Path (without Edges).
     */
    @Nonnull
    private PathMeta getPathForAttractionPair(
            AttractionEntity startAttraction,
            AttractionEntity endAttraction
    ) {
        PathMetaEntity pathMetaEntity;
        try {
            pathMetaEntity = pathMetaStore.findSuitablePath(
                    startAttraction.getNodeId(),
                    endAttraction.getNodeId()
            );
            pathMetaEntity.withHasEdges(pathStore.getEdgeCount(pathMetaEntity.getId()) > 0);
        } catch (NoResultException nre)  {
            pathMetaEntity = createNewPathMetaEntity(startAttraction, endAttraction);
        } catch (RuntimeException re) {
            LOGGER.error("Retrieving path from {} ({}) to {} ({}):",
                    startAttraction.getName(),
                    startAttraction.getNodeId(),
                    endAttraction.getName(),
                    endAttraction.getNodeId(),
                    re);
            throw re;
        }

        return pathMetaEntity.build();
    }

    private PathMetaEntity createNewPathMetaEntity(AttractionEntity startAttraction, AttractionEntity endAttraction) {
        String name = startAttraction.getName() +
                " - " +
                endAttraction.getName();

        PathMetaEntity pathMetaEntity = PathMetaEntity.builder()
                .withStartNodeId(startAttraction.getNodeId())
                .withEndNodeId(endAttraction.getNodeId())
                .withHasEdges(false)
                .withName(name);

        return pathMetaStore.createNew(pathMetaEntity);
    }

}
