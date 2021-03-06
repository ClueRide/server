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

import com.clueride.domain.course.Course;
import com.clueride.domain.location.latlon.LatLonEntity;
import com.clueride.domain.location.loclink.LocLink;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Set;

/**
 * Defines operations on {@link Location} instances.
 */
public interface LocationService {

    Location getById(Integer locationId);

    /**
     * Given an existing Location, update properties as
     * provided in the passed Builder instance. This expects
     * the Location ID to have been populated; otherwise, the
     * instance doesn't represent an existing location.
     * @param locationEntity instance with updated properties.
     * @return the updated and built Location.
     */
    Location updateLocation(LocationEntity locationEntity) throws MalformedURLException;

    /**
     * Given coordinates for a new location, create an instance with
     * default properties and persist this for showing on the map
     * as a Location needing attention. Generally, the client will
     * be opening a dialog to capture the properties that would be
     * passed to the {@link #updateLocation(LocationEntity)} endpoint.
     * @param latLonEntity representing the coordinates of the new Location.
     * @return Location instance with an assigned ID.
     */
    Location proposeLocation(LatLonEntity latLonEntity);

    /**
     * For an open session (which will have an Outing & Course),
     * return the list of ordered Locations that support the {@link Course}.
     * @return List of Location for the current session's Course; empty list
     * if no session or Course is available.
     */
    List<Location> getSessionLocationsWithGeoJson();

    /**
     * For editors, we return all nearby locations.
     * @param lat Latitude to match.
     * @param lon Longitude to match.
     * @return List of locations closest to the given location.
     */
    List<Location> getNearestMarkerLocations(Double lat, Double lon);

    /**
     * Deletes the Location record -- along with any links to Image records
     * (but not the images themselves).
     * @param locationId unique identifier for the Location.
     * @return The instance deleted.
     */
    Location deleteById(Integer locationId);

    /**
     * Designates an existing image to be the featured image for the Location.
     * @param locationId unique identifier for the Location.
     * @param imageId unique identifier for an existing Image.
     * @return the updated Location instance.
     */
    Location linkFeaturedImage(Integer locationId, Integer imageId) throws MalformedURLException;

    /**
     * Drops the link between this Location and the Featured Image.
     * @param locationId unique Identifier for the Location whose featured image we want to unlink.
     * @return The updated and re-evaluated Location.
     */
    Location unlinkFeaturedImage(Integer locationId) throws MalformedURLException;

    /**
     * Connects an established Location with an established LocLink record.
     * @param locationId unique identifier for the Location.
     * @param locLinkId unique identifier for the LocLink.
     * @return Updated Location instance.
     */
    Location linkMainLocLink(Integer locationId, Integer locLinkId);

    /**
     * Given a Location ID, retrieve the list of Location Links for that Location.
     * @param locationId unique identifier for the Location -- must not be null.
     * @return List of Location Links for the Location. May be empty; will not include the main link.
     */
    List<LocLink> getLocationLinksByLocation(Integer locationId);

    /**
     * Retrieves List of Locations expected to be part of a Theme.
     * @return List of Themed Locations.
     */
    List<Location> getThemeLocations();

    /**
     * Retrieves List of Attractions associated with the given Course.
     *
     * @param courseId unique identifier for the desired Course.
     * @return List of Attractions for the Course in the order defined by the Course.
     */
    Set<Location> getAttractionsForCourse(Integer courseId);

}
