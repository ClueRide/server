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
 * Created by jett on 1/27/19.
 */
package com.clueride.domain.location;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.clueride.auth.Secured;
import com.clueride.domain.location.latlon.LatLon;
import com.clueride.domain.location.loctype.LocationType;
import com.clueride.domain.location.loctype.LocationTypeService;

/**
 * REST API for {@link Location} instances.
 */
@Path("location")
public class LocationWebService {

    @Inject
    private LocationService locationService;
    @Inject
    private LocationTypeService locationTypeService;

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Location getById(@QueryParam("id") Integer locationId) {
        return locationService.getById(locationId);
    }

    @GET
    @Secured
    @Path("active")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Location> getActiveSessionLocations() {
        return locationService.getSessionLocationsWithGeoJson();
    }

    @POST
    @Secured
    @Path("update")
    @Produces(MediaType.APPLICATION_JSON)
    public Location updateLocation(LocationBuilder locationBuilder) {
        return locationService.updateLocation(locationBuilder);
    }

    @POST
    @Secured
    @Path("propose")
    @Produces(MediaType.APPLICATION_JSON)
    public Location proposeLocation(
            @QueryParam("lat") Double lat,
            @QueryParam("lon") Double lon
    ) {
        LatLon latLon = new LatLon(lat, lon);
        return locationService.proposeLocation(latLon);
    }

    @GET
    @Secured
    @Path("nearest-marker")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Location> getNearestMarkerLocations(
            @QueryParam("lat") Double lat,
            @QueryParam("lon") Double lon
    ) {
        return locationService.getNearestMarkerLocations(lat, lon);
    }

    @GET
    @Secured
    @Path("types")
    @Produces(MediaType.APPLICATION_JSON)
    public List<LocationType> getLocationTypes() {
        return locationTypeService.getLocationTypes();
    }

    @DELETE
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Location deleteById(@QueryParam("id") Integer locationId) {
        return locationService.deleteById(locationId);
    }

    @DELETE
    @Secured
    @Path("featured/")
    @Produces(MediaType.APPLICATION_JSON)
    public Location unlinkFeaturedImage(@QueryParam("id") Integer locationId) {
        return locationService.unlinkFeaturedImage(locationId);
    }

}
