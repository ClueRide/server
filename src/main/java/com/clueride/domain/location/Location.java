/**
 *   Copyright 2015 Jett Marks
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created Aug 15, 2015
 */
package com.clueride.domain.location;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.clueride.domain.course.Step;
import com.clueride.domain.location.latlon.LatLon;
import com.clueride.domain.location.loctype.LocationType;
import com.clueride.domain.puzzle.PuzzleBuilder;

/**
 * Holds the data for a Location, and provides some of the logic to provide derived properties.
 *
 * @author jett
 */
@Immutable
public class Location implements Step {
    private final Integer id;
    private final String name;
    private final String description;
    private final LocationType locationType;
    private final Integer nodeId;
    private final URL featuredImage;
    private final Integer googlePlaceId;
    private final LatLon latLon;
    private final ReadinessLevel readinessLevel;
    private List<PuzzleBuilder> puzzleBuilders;
    private final List<URL> imageUrls;
    private final Integer locationGroupId;
    private final String establishment;
    private final Integer establishmentId;
    private final Map<String,Optional<Double>> tagScores;
    private final static Integer SYNCH_LOCK = -1;

    /**
     * Constructor accepting Builder instance.
     * @param builder instance carrying mutable Location.
     */
    public Location(LocationBuilder builder) {
        id = builder.getId();
        nodeId = builder.getNodeId();
        latLon = builder.getLatLon();
        readinessLevel = builder.getReadinessLevel();

        // If any of these are missing, we're at the Draft level
        name = builder.getName();
        description = builder.getDescription();
        locationType = builder.getLocationType();
        featuredImage = builder.getFeaturedImage();

        // If we have multiple images, the ranking goes up
        imageUrls = builder.getImageUrls();

        // Featured Level requires the following
        googlePlaceId = builder.getGooglePlaceId();

        // Possibly empty list
        tagScores = builder.getTagScores();

        // Optional values
        locationGroupId = builder.getLocationGroupId();
        establishmentId = builder.getEstablishmentId();
        establishment = builder.getEstablishment();
    }

    public Integer getId() {
        return id;
    }

    /**
     * Human readable name for this location.
     * Shows up in headings for the Location, Lists of Locations, and as pop-ups on the map.
     * @return String representing human-readable name of Location.
     */
    public String getName() {
        return name;
    }

    /**
     * Textual description of this Location.
     * Describes why this location is interesting.
     * @return String representing a description of this Location.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Enumeration of the type of Location.
     * Plays a role in helping select locations and categorizes them.
     * @return Enumeration of the type of Location.
     */
    public Integer getLocationTypeId() {
        return (locationType == null ? 0 : locationType.getId());
    }

    public String getLocationTypeName() {
        return (locationType == null ? "none" : locationType.getName());
    }

    public LocationType getLocationType() {
        return locationType;
    }

    /**
     * Link over to the geographical representation of this Location.
     * @return ID of the LatLon associated with this location for placement on a map.
     */
    public Integer getNodeId() {
        return nodeId;
    }

    public LatLon getLatLon() {
        return latLon;
    }

    public Map<String, Optional<Double>> getTagScores() {
        return tagScores;
    }

    /**
     * This method returns null for an empty Location Group to
     * accommodate the JSON writer, but not sure this is what
     * we'll continue to want.
     * @return Integer or null if not present.
     */
    public Integer getLocationGroupId() {
        if(locationGroupId != null) {
            return locationGroupId;
        } else {
            return null;
        }
    }

    public Integer getEstablishment() {
        return establishmentId;
    }

    public URL getFeaturedImage() {
        return featuredImage;
    }

    public List<URL> getImageUrls() {
        return imageUrls;
    }

    /**
     * Determines progress against criteria described here: http://bikehighways.wikidot.com/clueride-location-details
     * @return Readiness level based on completeness of the fields for this object.
     */
    public ReadinessLevel getReadinessLevel() {
        return readinessLevel;
    }

    public Integer getGooglePlaceId() {
        return googlePlaceId;
    }

    /**
     * List of Strings that in addition to LocationType, each categorize the location.
     * Perhaps a placeholder at this time until I think through further what sorts of tags might be applied.
     * @return Set of Strings, one for each tag added to this location.
    public Set<String> getTags() {
    return tagScores.keySet();
    }
     */

    // TODO: not settled on this API
    public Optional<Double> getScorePerTag(String tag) {
        return tagScores.get(tag);
    }

    // TODO: CA-324 -- Move this logic into the service
    public void removePuzzle(Integer puzzleId) {
        synchronized(SYNCH_LOCK) {
            List<PuzzleBuilder> remainingPuzzles = new ArrayList<>();
            for (PuzzleBuilder builder : puzzleBuilders) {
                if (!puzzleId.equals(builder.getId()) && builder != null) {
                    remainingPuzzles.add(builder);
                }
            }
            this.puzzleBuilders = ImmutableList.copyOf(remainingPuzzles);
        }
    }

    public List<PuzzleBuilder> getPuzzleBuilders() {
        return puzzleBuilders;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
