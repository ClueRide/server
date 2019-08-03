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

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clueride.aop.badge.BadgeCapture;
import com.clueride.auth.session.ClueRideSession;
import com.clueride.auth.session.ClueRideSessionDto;
import com.clueride.domain.course.CourseService;
import com.clueride.domain.image.ImageLinkEntity;
import com.clueride.domain.image.ImageStore;
import com.clueride.domain.location.latlon.LatLon;
import com.clueride.domain.location.latlon.LatLonService;
import com.clueride.domain.location.loclink.LocLink;
import com.clueride.domain.location.loclink.LocLinkEntity;
import com.clueride.domain.location.loclink.LocLinkService;
import com.clueride.domain.location.loctype.LocationType;
import com.clueride.domain.location.loctype.LocationTypeService;
import com.clueride.domain.outing.OutingView;
import com.clueride.domain.place.ScoredLocationService;
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
    private final LocationTypeService locationTypeService;
    private final ImageStore imageStore;
    private final LocLinkService locLinkService;

    @Inject
    public LocationServiceImpl(
            CourseService courseService,
            LocationStore locationStore,
            LatLonService latLonService,
            ScoredLocationService scoredLocationService,
            LocationTypeService locationTypeService,
            ImageStore imageStore,
            LocLinkService locLinkService
    ) {
        this.courseService = courseService;
        this.locationStore = locationStore;
        this.latLonService = latLonService;
        this.scoredLocationService = scoredLocationService;
        this.locationTypeService = locationTypeService;
        this.imageStore = imageStore;
        this.locLinkService = locLinkService;
    }

    @Override
    public Location getById(Integer locationId) {
        LocationBuilder builder = locationStore.getLocationBuilderById(locationId);
        return builder.build();
    }

    @Override
    public Location proposeLocation(LatLon latLon) {
        latLonService.addNew(latLon);
        // TODO: SVR-36 Tidy LocType
        LocationType locationType = locationTypeService.getById(0);
        LocationBuilder locationBuilder = LocationBuilder.builder()
                .withLatLon(latLon)
                .withLocationType(locationType);
        try {
            locationStore.addNew(locationBuilder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return locationBuilder.build();
    }

    @Override
    @BadgeCapture
    public Location updateLocation(LocationBuilder locationBuilder) {
        requireNonNull(locationBuilder.getId(), "Location ID not found; cannot update non-existent record");
        requireNonNull(locationBuilder.getLocationTypeId(), "Location Type not specified; cannot update");
        // TODO: SVR-36 Tidy LocType
        locationBuilder.withLocationType(locationTypeService.getById(locationBuilder.getLocationTypeId()));
        locationBuilder.withReadinessLevel(scoredLocationService.calculateReadinessLevel(locationBuilder));

        // TODO: SVR-36 and this too -- unit testing will help flatten this
        if (locationBuilder.getMainLink().isPresent()) {
            LocLinkEntity proposedLocLinkEntity = locationBuilder.getMainLink().get();
            if (proposedLocLinkEntity.getId() == null) {
                try {
                    LocLink locLink  = locLinkService.createNewLocationLink(proposedLocLinkEntity);
                    locationBuilder.withLocLink(locLink);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else if (proposedLocLinkEntity.getLink().length() > 0) {
                try {
                    LocLink locLink = locLinkService.getLocLinkByUrl(
                            proposedLocLinkEntity.getLink()
                    );
                    locationBuilder.withLocLink(locLink);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        locationStore.update(locationBuilder);
        return locationBuilder.build();
    }

    @Override
    public List<Location> getSessionLocationsWithGeoJson() {
        List<Location> locations = new ArrayList<>();
        OutingView outingView = clueRideSessionDto.getOutingView();
        List<Integer> locationIds = courseService.getLocationIdsForCourse(outingView.getCourseId());
        for (Integer locationId : locationIds) {
            LocationBuilder locationBuilder = locationStore.getLocationBuilderById(locationId);

            // TODO: When GeoJSON out of the DB is available for Nodes, use that instead of this:
            LatLon latLon = latLonService.getLatLonById(locationBuilder.getNodeId());
            locationBuilder.withLatLon(latLon);
            locations.add(locationBuilder.build());
        }

        return locations;
    }

    @Override
    public List<Location> getNearestMarkerLocations(Double lat, Double lon) {
        LOGGER.info("Retrieving Nearest Marker Locations for (" + lat + ", " + lon + ")");
        List<Location> locations = new ArrayList<>();

        for (LocationBuilder builder : locationStore.getLocationBuilders()) {
            // TODO: LE-76 Layer Thing going on right here:
            if (!builder.getLocationTypeBuilder().getId().equals(15)) {
                fillAndGradeLocation(builder);
                locations.add(builder.build());
            }
        }
        return locations;
    }

    @Override
    public Location deleteById(Integer locationId) {
        LOGGER.info("Deleting Location with ID {}", locationId);

        LocationBuilder locationBuilder = locationStore.getLocationBuilderById(locationId);
        locationStore.delete(locationBuilder);
        return locationBuilder.build();
    }

    @Override
    public Location linkFeaturedImage(Integer locationId, Integer imageId) {
        requireNonNull(locationId, "Location ID required");
        requireNonNull(imageId, "Image ID required");
        LocationBuilder locationBuilder = locationStore.getLocationBuilderById(locationId);
        /* First, break any existing link. */
        if (!locationBuilder.hasNoFeaturedImage()) {
            locationBuilder.clearFeaturedImage();
        }
        // TODO: SVR-36 Tidy LocType
        locationBuilder.withLocationTypeId(locationBuilder.getLocationTypeBuilder().getId());

        ImageLinkEntity imageLinkEntity = imageStore.getImageLink(imageId);
        locationBuilder.withFeaturedImage(imageLinkEntity);
        return updateLocation(locationBuilder);
    }

    @Override
    public Location unlinkFeaturedImage(Integer locationId) {
        requireNonNull(locationId, "Location ID required");
        LocationBuilder locationBuilder = locationStore.getLocationBuilderById(locationId);

        // TODO: SVR-36 Tidy LocType
        locationBuilder.withLocationType(locationTypeService.getById(
                locationBuilder.getLocationTypeBuilder().getId()
        ));
        locationBuilder.withLocationTypeId(locationBuilder.getLocationTypeBuilder().getId());

        locationBuilder.clearFeaturedImage();
        /* Update Location will set new readiness level and persist the updated Location. */
        return updateLocation(locationBuilder);
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

        for (LocationBuilder builder : locationStore.getThemedLocationBuilders()) {
            /* For these Location instances, what we certainly need is the IDs and the Type.
             * Unclear if we need the readiness level at this time.
             */
            locations.add(builder.build());
        }
        return locations;
    }

    // TODO: SVR-36 Tidy LocType (maybe)
    private void fillAndGradeLocation(LocationBuilder builder) {
        /* Assemble the derived transient fields. */
        builder.withLatLon(latLonService.getLatLonById(builder.getNodeId()));

        /* Last thing to assemble; after other pieces have been put into place. */
        builder.withReadinessLevel(scoredLocationService.calculateReadinessLevel(builder));
    }

}
