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
 * Created by jett on 1/13/19.
 */
package com.clueride.domain.location;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.google.common.base.Optional;

import com.clueride.domain.location.latlon.LatLon;
import com.clueride.domain.location.loctype.LocationType;
import com.clueride.domain.puzzle.PuzzleBuilder;

/**
 * Knows how to assemble the parts of a Location.
 */
@Entity(name="location")
public class LocationBuilder {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="location_pk_sequence")
    @SequenceGenerator(name="location_pk_sequence",sequenceName="location_id_seq", allocationSize=1)
    private Integer id;

    private String name;
    private String description;

    @Column(name= "location_type_id") private Integer locationTypeId;

    @Column(name="node_id") private Integer nodeId;
    @Column(name="featured_image_id") private Integer featuredImageId;

    @OneToMany(mappedBy = "locationBuilder")
    private List<PuzzleBuilder> puzzleBuilders;

    // TODO: CA-325 - Coming out after we abandon the Json Clues
    @Transient
    private List<Integer> clueIds;

    @Transient
    private LocationType locationType;
    @Transient
    private String locationTypeName;
    @Transient
    private LatLon latLon;
    @Transient
    private List<URL> imageUrls;
    @Transient
    private URL featuredImage;
    @Transient
    private Integer googlePlaceId;

    @Transient
    private Integer establishmentId;
    @Column(name="location_group_id") private Integer locationGroupId;

    @Transient
    private Map<String,Optional<Double>> tagScores = new HashMap<>();

    @Transient
    private String establishment;

    @Transient
    private ReadinessLevel readinessLevel;

    public LocationBuilder() {}

    public static LocationBuilder builder() {
        return new LocationBuilder();
    }

    public static LocationBuilder from(Location location) {
        return builder()
                .withId(location.getId())
                .withName(location.getName())
                .withDescription(location.getDescription())
                .withLocationType(location.getLocationType())
                .withNodeId(location.getNodeId())
                .withLatLon(location.getLatLon())
                .withPuzzleBuilders(location.getPuzzleBuilders())
                .withFeaturedImage(location.getFeaturedImage())
                .withImageUrls(location.getImageUrls())
                .withEstablishmentId(location.getEstablishment())
                .withTagScores(location.getTagScores())
                ;
    }

    public Location build() {
        if (locationType == null) {
            throw new IllegalStateException("Location Type cannot be null");
        }
        return new Location(this);
    }

    public LocationBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * If the value has been set (by reading from the LocationStore), use that
     * value, otherwise, we're creating a new instance the and idProvider should
     * give us the next available value.
     * @return Unique ID for this instance of Location.
     */
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocationBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public LocationBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public LocationType getLocationType() {
        return locationType;
    }

    public LocationBuilder withLocationTypeId(Integer locationTypeId) {
        this.locationTypeId = locationTypeId;
        return this;
    }

    public Integer getLocationTypeId() {
        return locationTypeId;
    }

    public LocationBuilder withLocationTypeName(String locationTypeName) {
        this.locationTypeName = locationTypeName;
        return this;
    }

    public String getLocationTypeName() {
        return this.locationTypeName;
    }

    public void setLocationType(LocationType locationType) {
        this.withLocationType(locationType);
    }

    public LocationBuilder withLocationType(LocationType locationType) {
        this.locationType = locationType;
        this.locationTypeId = locationType.getId();
        return this;
    }

    public LocationBuilder withReadinessLevel(ReadinessLevel readinessLevel) {
        this.readinessLevel = readinessLevel;
        return this;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public LocationBuilder withNodeId(Integer nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public List<PuzzleBuilder> getPuzzleBuilders() {
        return puzzleBuilders;
    }

    public LocationBuilder withPuzzleBuilders(List<PuzzleBuilder> puzzleBuilders) {
        this.puzzleBuilders = puzzleBuilders;
        return this;
    }

    public LocationBuilder addPuzzleBuilder(PuzzleBuilder puzzleBuilder) {
        this.puzzleBuilders.add(puzzleBuilder);
        return this;
    }

    public Map<String, Optional<Double>> getTagScores() {
        return tagScores;
    }

    public LocationBuilder withTagScores(Map<String, Optional<Double>> tagScores) {
        this.tagScores = tagScores;
        return this;
    }

    public Integer getLocationGroupId() {
        return locationGroupId;
    }

    public LocationBuilder withLocationGroupId(Integer locationGroupId) {
        this.locationGroupId = locationGroupId;
        return this;
    }

    public Integer getEstablishmentId() {
        return establishmentId;
    }

    public LocationBuilder withEstablishmentId(Integer establishmentId) {
        this.establishmentId = establishmentId;
        return this;
    }

    public String getEstablishment() {
        return establishment;
    }

    public LocationBuilder withEstablishment(String establishment) {
        this.establishment = establishment;
        return this;
    }

    public List<URL> getImageUrls() {
        if (imageUrls != null) {
            return imageUrls;
        } else {
            return Collections.emptyList();
        }
    }

    public LocationBuilder withImageUrls(List<URL> imageUrls) {
        this.imageUrls = imageUrls;
        return this;
    }

    public LatLon getLatLon() {
        return latLon;
    }

    public LocationBuilder withLatLon(LatLon latLon) {
        this.latLon = latLon;
        if (latLon != null) {
            this.nodeId = latLon.getId();
        }
        return this;
    }

    public URL getFeaturedImage() {
        return featuredImage;
    }

    public LocationBuilder withFeaturedImage(URL featuredImage) {
        this.featuredImage = featuredImage;
        return this;
    }

    public LocationBuilder clearFeaturedImage() {
        this.featuredImageId = null;
        this.featuredImage = null;
        return this;
    }

    public boolean hasNoFeaturedImage() {
        return featuredImageId == null;
    }

    public Integer getFeaturedImageId() {
        return featuredImageId;
    }

    public LocationBuilder withFeaturedImageId(int imageId) {
        this.featuredImageId = imageId;
        return this;
    }

    public Integer getGooglePlaceId() {
        return googlePlaceId;
    }

    public LocationBuilder withGooglePlaceId(Integer googlePlaceId) {
        this.googlePlaceId = googlePlaceId;
        return this;
    }

    public ReadinessLevel getReadinessLevel() {
        return readinessLevel;
    }

    /**
     * Accepts a partial set of information -- generally posted from REST API -- and updates this copy with the
     * new fields.
     * @param locationBuilder instance with the new info.
     */
    public void updateFrom(LocationBuilder locationBuilder) {
        this
                .withName(locationBuilder.name)
                .withId(locationBuilder.id)
                .withDescription(locationBuilder.description)
                .withNodeId(locationBuilder.nodeId)
                .withLocationTypeId(locationBuilder.locationTypeId)
                .withLocationGroupId(locationBuilder.locationGroupId)
                .withImageUrls(locationBuilder.imageUrls);
    }

    /* TODO: CA-325 Only useful for moving from JSON to JPA; remove after JSON is abandoned for Clues. */
    public List<Integer> getClueIds() {
        return clueIds;
    }

    public LocationBuilder withClueIds(List<Integer> clueIds) {
        this.clueIds = clueIds;
        return this;
    }
}
