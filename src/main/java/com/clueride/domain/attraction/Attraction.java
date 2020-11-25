/**
 * Copyright 2020 Jett Marks
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
 * Created May 5, 2020
 */
package com.clueride.domain.attraction;

import com.clueride.domain.flag.Flag;
import com.clueride.domain.image.ImageLink;
import com.clueride.domain.location.LocationEntity;
import com.clueride.domain.location.ReadinessLevel;
import com.clueride.domain.location.latlon.LatLonEntity;
import com.clueride.domain.location.loclink.LocLink;
import com.clueride.domain.puzzle.PuzzleEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.concurrent.Immutable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Holds the data for a Location presented both to the Game Player as well as Editors.
 *
 * {@link LocationEntity} holds mutable instances.
 *
 * @author jett
 */
@Immutable
public class Attraction {
    private final int id;
    private final int nodeId;
    private final String name;
    private final String description;
    private final Integer locationTypeId;
    private final String locationTypeName;
    private final String locationTypeIcon;
    private final ImageLink featuredImage;
    private final LocLink mainLink;
    private final Integer googlePlaceId;
    private final LatLonEntity latLon;
    private final ReadinessLevel readinessLevel;
    private List<PuzzleEntity> puzzleEntities;
    private final Integer locationGroupId;
    private final String establishment;
    private final Integer establishmentId;
    private final Map<String, Optional<Double>> tagScores;
    private final List<Flag> flags;

    /**
     * Constructor accepting Entity instance.
     *
     * @param entity {@link AttractionEntity} instance carrying mutable Attraction.
     */
    public Attraction(AttractionEntity entity) {
        id = entity.getId();
        nodeId = entity.getNodeId();
        latLon = entity.getLatLonEntity();
        readinessLevel = entity.getReadinessLevel();

        // If any of these are missing, we're at the Draft level
        name = entity.getName();
        description = entity.getDescription();
        locationTypeId = entity.getLocationTypeId();
        locationTypeName = entity.getLocationTypeName();
        locationTypeIcon = entity.getLocationTypeIcon();

        /* OK for Location to be missing Featured Image. */
        if (entity.getFeaturedImage() != null) {
            featuredImage = entity.getFeaturedImage().build();
        } else {
            featuredImage = null;
        }

        /* OK for Location to be missing its Main Link. */
        if (entity.getMainLink().isPresent()) {
            mainLink = entity.getMainLink().get().build();
        } else {
            mainLink = null;
        }

        // Possibly empty list
        flags = entity.getFlags();

        // Featured Level requires the following
        googlePlaceId = entity.getGooglePlaceId();

        // Possibly empty list
        tagScores = entity.getTagScores();

        // Optional values
        locationGroupId = entity.getLocationGroupId();
        establishmentId = entity.getEstablishmentId();
        establishment = entity.getEstablishment();
    }

    public int getId() {
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
        return (locationTypeId);
    }

    public String getLocationTypeName() {
        return locationTypeName;
    }

    public String getLocationTypeIconName() {
        return locationTypeIcon;
    }

    /**
     * Link over to the geographical representation of this Location.
     * @return ID of the LatLonEntity associated with this location for placement on a map.
     */
    public Integer getNodeId() {
        return nodeId;
    }

    public LatLonEntity getLatLon() {
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

    public ImageLink getFeaturedImage() {
        return featuredImage;
    }

    public LocLink getMainLink() {
        return mainLink;
    }

    public List<Flag> getFlags() {
        return flags;
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
