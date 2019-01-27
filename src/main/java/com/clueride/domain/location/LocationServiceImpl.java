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

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

import com.clueride.auth.ClueRideSession;
import com.clueride.auth.ClueRideSessionDto;
import com.clueride.domain.course.CourseService;
import com.clueride.domain.location.latlon.LatLon;
import com.clueride.domain.location.latlon.LatLonService;
import com.clueride.domain.outing.OutingView;

/**
 * Implementation of {@link LocationService}.
 */
public class LocationServiceImpl implements LocationService {

    @Inject
    @SessionScoped
    @ClueRideSession
    private ClueRideSessionDto clueRideSessionDto;

    private final CourseService courseService;
    private final LocationStore locationStore;
    private final LatLonService latLonService;

    @Inject
    public LocationServiceImpl(
            CourseService courseService,
            LocationStore locationStore,
            LatLonService latLonService
    ) {
        this.courseService = courseService;
        this.locationStore = locationStore;
        this.latLonService = latLonService;
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
}
