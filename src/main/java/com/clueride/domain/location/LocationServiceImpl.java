/*
 * Copyright 2019 Jett Marks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by jett on 1/26/19.
 */
package com.clueride.domain.location;

import com.clueride.aop.badge.BadgeCapture;
import com.clueride.auth.session.ClueRideSession;
import com.clueride.auth.session.ClueRideSessionDto;
import com.clueride.domain.course.CourseService;
import com.clueride.domain.image.ImageLinkEntity;
import com.clueride.domain.image.ImageStore;
import com.clueride.domain.location.latlon.LatLonEntity;
import com.clueride.domain.location.latlon.LatLonService;
import com.clueride.domain.location.loclink.LocLink;
import com.clueride.domain.location.loclink.LocLinkEntity;
import com.clueride.domain.location.loclink.LocLinkService;
import com.clueride.domain.outing.OutingView;
import com.clueride.domain.place.ScoredLocationService;
import com.clueride.domain.puzzle.PuzzleService;
import org.slf4j.Logger;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Objects.requireNonNull;

/**
 * Implementation of {@link LocationService}.
 */
public class LocationServiceImpl implements LocationService {
    @Inject
    private Logger LOGGER;

    @Inject
    @SessionScoped
    @ClueRideSession
    private ClueRideSessionDto clueRideSessionDto;

    private final CourseService courseService;
    private final LocationStore locationStore;
    private final LatLonService latLonService;
    private final ScoredLocationService scoredLocationService;
    private final ImageStore imageStore;
    private final PuzzleService puzzleService;
    private final LocLinkService locLinkService;

    @Inject
    public LocationServiceImpl(
            CourseService courseService,
            LocationStore locationStore,
            LatLonService latLonService,
            ScoredLocationService scoredLocationService,
            ImageStore imageStore,
            PuzzleService puzzleService,
            LocLinkService locLinkService
    ) {
        this.courseService = courseService;
        this.locationStore = locationStore;
        this.latLonService = latLonService;
        this.scoredLocationService = scoredLocationService;
        this.imageStore = imageStore;
        this.puzzleService = puzzleService;
        this.locLinkService = locLinkService;
    }

    @Override
    public Location getById(Integer locationId) {
        LocationEntity builder = locationStore.getLocationBuilderById(locationId);
        return builder.build();
    }

    @Override
    public Location proposeLocation(LatLonEntity latLonEntity) {
        latLonService.addNew(latLonEntity);
        LocationEntity locationEntity = LocationEntity.builder()
                .withLatLon(latLonEntity)
                .withLocationTypeId(0);
        try {
            locationStore.addNew(locationEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locationEntity.build();
    }

    @Override
    @BadgeCapture
    public Location updateLocation(LocationEntity locationEntity) throws MalformedURLException {
        LOGGER.debug("Updating Location");
        requireNonNull(locationEntity.getId(), "Location ID not found; cannot update non-existent record");
        requireNonNull(locationEntity.getLocationTypeId(), "Location Type ID not specified; cannot update");
        locationEntity.withReadinessLevel(scoredLocationService.calculateReadinessLevel(locationEntity));
        locationEntity.withLatLon(latLonService.getLatLonById(locationEntity.getNodeId()));

        // TODO: SVR-94
        if (locationEntity.getMainLink().isPresent()) {
            LocLinkEntity proposedLocLinkEntity = locationEntity.getMainLink().get();
            LocLinkEntity persistedLocLinkEntity = locLinkService.validateAndPrepareFromUserInput(proposedLocLinkEntity);
            locationEntity.withLocLink(persistedLocLinkEntity);
        }

        locationStore.update(locationEntity);
        return locationEntity.build();
    }

    @Override
    public List<Location> getSessionLocationsWithGeoJson() {
        List<Location> locations = new ArrayList<>();
        OutingView outingView = clueRideSessionDto.getOutingView();
        List<Integer> locationIds = courseService.getAttractionIdsForCourse(outingView.getCourseId());
        for (Integer locationId : locationIds) {
            LocationEntity locationEntity = locationStore.getLocationBuilderById(locationId);

            // TODO: When GeoJSON out of the DB is available for Nodes, use that instead of this:
            LatLonEntity latLonEntity = latLonService.getLatLonById(locationEntity.getNodeId());
            locationEntity.withLatLon(latLonEntity);
            locations.add(locationEntity.build());
        }

        return locations;
    }

    @Override
    public List<Location> getNearestMarkerLocations(Double lat, Double lon) {
        LOGGER.info("Retrieving Nearest Marker Locations for (" + lat + ", " + lon + ")");
        List<Location> locations = new ArrayList<>();

        for (LocationEntity builder : locationStore.getLocationBuilders()) {
            // TODO: CI-73 Layer Thing going on right here:
            if (!builder.getLocationTypeId().equals(15)) {
                fillAndGradeLocation(builder);
                locations.add(builder.build());
            }
        }
        return locations;
    }

    @Override
    public Location deleteById(Integer locationId) {
        LOGGER.info("Deleting Location with ID {}", locationId);

        LocationEntity locationEntity = locationStore.getLocationBuilderById(locationId);
        if (locationEntity != null) {
            /* Drop all images & puzzles. */
            imageStore.releaseImagesForLocation(locationId);
            puzzleService.removeByLocation(locationEntity);

            locationStore.delete(locationEntity);
            return locationEntity.build();
        } else {
            LOGGER.warn("Attempted Deletion of Location that doesn't exist ({})", locationId);
            return null;
        }
    }

    @Override
    public Location linkFeaturedImage(Integer locationId, Integer imageId) throws MalformedURLException {
        requireNonNull(locationId, "Location ID required");
        requireNonNull(imageId, "Image ID required");
        LocationEntity locationEntity = locationStore.getLocationBuilderById(locationId);

        /* First, break any existing link. */
        if (!locationEntity.hasNoFeaturedImage()) {
            locationEntity.clearFeaturedImage();
        }

        ImageLinkEntity imageLinkEntity = imageStore.getImageLink(imageId);
        locationEntity.withFeaturedImage(imageLinkEntity);
        return updateLocation(locationEntity);
    }

    @Override
    public Location unlinkFeaturedImage(Integer locationId) throws MalformedURLException {
        requireNonNull(locationId, "Location ID required");
        LocationEntity locationEntity = locationStore.getLocationBuilderById(locationId);
        locationEntity.clearFeaturedImage();
        /* Update Location will set new readiness level and persist the updated Location. */
        return updateLocation(locationEntity);
    }

    @Override
    public Location linkMainLocLink(Integer locationId, Integer locLinkId) {
        return null;
    }

    @Override
    public List<LocLink> getLocationLinksByLocation(Integer locationId) {
        return null;
    }

    @Override
    public List<Location> getThemeLocations() {
        LOGGER.info("Retrieving Themed Locations");
        List<Location> locations = new ArrayList<>();

        for (LocationEntity builder : locationStore.getThemedLocationBuilders()) {
            /* For these Location instances, what we certainly need is the IDs and the Type.
             * Unclear if we need the readiness level at this time.
             */
            locations.add(builder.build());
        }
        return locations;
    }

    @Override
    public List<Location> getAttractionsForCourse(Integer courseId) {
        LOGGER.debug("Retrieving Attractions for Course {}", courseId);
        List<Location> attractions = new ArrayList<>();
        Set<Integer> loadedAttractionIds = new HashSet<>();

        /* Course Service knows which Attraction IDs are associated with the Course. */
        List<Integer> attractionIds = courseService.getAttractionIdsForCourse(courseId);
        for (Integer attractionId : attractionIds) {
            /* Prevent duplicate entries for courses that hit the same Attraction more than once. */
            if (!loadedAttractionIds.contains(attractionId)) {
                LocationEntity builder = locationStore.getLocationBuilderById(attractionId);
                fillAndGradeLocation(builder);
                attractions.add(builder.build());
                loadedAttractionIds.add(attractionId);
            }
        }
        return attractions;
    }

    private void fillAndGradeLocation(LocationEntity builder) {
        /* Assemble the derived transient fields. */
        builder.withLatLon(latLonService.getLatLonById(builder.getNodeId()));

        /* Last thing to assemble; after other pieces have been put into place. */
        builder.withReadinessLevel(scoredLocationService.calculateReadinessLevel(builder));
    }

}
