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

import java.net.MalformedURLException;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.clueride.auth.Secured;
import com.clueride.domain.location.latlon.LatLonEntity;
import com.clueride.domain.location.loclink.LocLink;
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

    /**
     * Retrieves a specific Location by ID.
     * @param locationId unique ID of the Location
     * @return {@link Location} instance matching the ID.
     */
    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Location getById(@QueryParam("id") Integer locationId) {
        return locationService.getById(locationId);
    }

    /**
     * This is the list of Locations that are attached to a session's Course.
     * @return List of {@link Location} for the active course.
     */
    @GET
    @Secured
    @Path("active")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Location> getActiveSessionLocations() {
        return locationService.getSessionLocationsWithGeoJson();
    }

    /**
     * Accepts updates for a given Location -- the Location ID is part of the Builder that is provided.
     * @param locationEntity Builder instance containing complete set of changes.
     * @return The updated {@link Location}instance.
     */
    @POST
    @Secured
    @Path("update")
    @Produces(MediaType.APPLICATION_JSON)
    public Location updateLocation(LocationEntity locationEntity) throws MalformedURLException {
        return locationService.updateLocation(locationEntity);
    }

    @GET
    @Secured
    @Path("{locationId}/links")
    @Produces(MediaType.APPLICATION_JSON)
    public List<LocLink> getLocationLinks(@PathParam("locationId") Integer locationId) {
        return locationService.getLocationLinksByLocation(locationId);
    }

    /**
     * Initializes a new Node along with a (mostly empty) Location at that Node.
     * @param lat Latitude of the new Node/Location.
     * @param lon Longitude of the new Node/Location.
     * @return new partially-filled Location; has enough information to at least show it on the map.
     */
    @POST
    @Secured
    @Path("propose")
    @Produces(MediaType.APPLICATION_JSON)
    public Location proposeLocation(
            @QueryParam("lat") Double lat,
            @QueryParam("lon") Double lon
    ) {
        LatLonEntity latLonEntity = new LatLonEntity(lat, lon);
        return locationService.proposeLocation(latLonEntity);
    }

    /**
     * Retrieves a list of all the locations, but eventually, will be just
     * the locations closest to the given lat/lon pair.
     * @param lat Latitude of the center of the locations to be retrieved.
     * @param lon Longitude of the center of the locations to be retrieved.
     * @return List of {@link Location} instances matching input.
     */
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

    @PUT
    @Secured
    @Path("featured/{locId}/{imageId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Location linkFeaturedImage(
            @PathParam("locId") Integer locationId,
            @PathParam("imageId") Integer imageId
    ) throws MalformedURLException {
        return locationService.linkFeaturedImage(locationId, imageId);
    }

    @DELETE
    @Secured
    @Path("featured/")
    @Produces(MediaType.APPLICATION_JSON)
    public Location unlinkFeaturedImage(@QueryParam("id") Integer locationId) throws MalformedURLException {
        return locationService.unlinkFeaturedImage(locationId);
    }

    // TODO: Is this being used?
    @PUT
    @Secured
    @Path("main/{locId}/{locLinkId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Location linkMainLocLink(
            @PathParam("locId") Integer locationId,
            @PathParam("locLinkId") Integer locLinkId
    ) {
        return locationService.linkMainLocLink(locationId, locLinkId);
    }

    @GET
    @Path("themed")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Location> getThemedLocations() {
        return locationService.getThemeLocations();
    }

}
