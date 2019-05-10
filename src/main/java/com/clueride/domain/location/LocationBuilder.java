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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.sun.istack.internal.Nullable;

import com.clueride.domain.image.ImageLinkEntity;
import com.clueride.domain.location.latlon.LatLon;
import com.clueride.domain.location.loclink.LocLink;
import com.clueride.domain.location.loclink.LocLinkEntity;
import com.clueride.domain.location.loctype.LocationType;
import com.clueride.domain.location.loctype.LocationTypeBuilder;
import com.clueride.domain.puzzle.PuzzleBuilder;

/**
 * Knows how to assemble the parts of a Location.
 *
 * In this incarnation, the target client is the Player application.
 * We may get mileage out of this for the Location Editor, but that's
 * not the focus at this date (Jan 2019).
 *
 * OK, on Feb 5 2019, we're looking at whether this will serve the
 * Location Editor ;-). Looks like the Puzzle Builders are considered to
 * determine the ReadinessLevel of the Location. I think we can consult
 * the Puzzle Service to find what we need.
 */
@Entity(name="location")
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationBuilder {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="location_pk_sequence")
    @SequenceGenerator(name="location_pk_sequence",sequenceName="location_id_seq", allocationSize=1)
    private Integer id;

    private String name;
    private String description;

    @Column(name="node_id") private Integer nodeId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="featured_image_id")
    private ImageLinkEntity featuredImage;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "main_link_id")
    private LocLinkEntity mainLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_type_id")
    private LocationTypeBuilder locationTypeBuilder;

    @Column(name="location_group_id") private Integer locationGroupId;

    @Transient
    private LocationType locationType;
    @Transient
    private String locationTypeName;
    @Transient
    private LatLon latLon;
    @Transient
    private URL featuredImageUrl;
    @Transient private Integer featuredImageId;
    @Transient
    private List<PuzzleBuilder> puzzleBuilders;
    @Transient
    private Integer googlePlaceId;

    @Transient
    private Integer establishmentId;

    @Transient
    @Nullable
    private Map<String, Optional<Double>> tagScores = new HashMap<>();

    @Transient
    private String establishment;

    @Transient
    private ReadinessLevel readinessLevel;
    @Transient
    private Integer locationTypeId;

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
                .withFeaturedImage(ImageLinkEntity.from(location.getFeaturedImage()))
                .withEstablishmentId(location.getEstablishment())
                .withTagScores(location.getTagScores())
                ;
    }

    public Location build() {
        /* Convert String to URL */
        if (featuredImage != null) {
            try {
                featuredImageId = featuredImage.getId();
                featuredImageUrl = new URL(featuredImage.getUrl());
            } catch (MalformedURLException e) {
                throw new RuntimeException(
                        "Got a bad URL in the Image table: "
                                + featuredImage.getUrl()
                );
            }
        }

        if (locationTypeBuilder == null) {
            throw new IllegalStateException("Location Type cannot be null");
        } else {
            locationType = locationTypeBuilder.build();
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

    @JsonGetter("locationType")
    public LocationType getLocationType() {
        return locationType;
    }

    /**
     * This is generally inbound from clients. Service is responsible for
     * taking this value, looking up the LocationType via its service, and
     * then populating this LocationBuilder instance with that LocationType
     * instance.
     * @param locationTypeId chosen from a list of possible Location Types.
     * @return this.
     */
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
        this.locationTypeBuilder = LocationTypeBuilder.from(locationType);
        return this;
    }

    public LocationTypeBuilder getLocationTypeBuilder() {
        return locationTypeBuilder;
    }

    @JsonSetter("locationType")
    public void setLocationTypeBuilder(LocationTypeBuilder locationTypeBuilder) {
        this.locationTypeBuilder = locationTypeBuilder;
        this.locationType = locationTypeBuilder.build();
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

    /* Image Methods */
    public ImageLinkEntity getFeaturedImage() {
        return featuredImage;
    }

    public LocationBuilder withFeaturedImage(ImageLinkEntity featuredImage) {
        this.featuredImage = featuredImage;
        this.featuredImageId = featuredImage.getId();
        try {
            this.featuredImageUrl = new URL(featuredImage.getUrl());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Bad URL: " + featuredImage.getUrl(), e);
        }
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

    public LocationBuilder withFeaturedImageUrl(URL featuredImageUrl) {
        this.featuredImageUrl = featuredImageUrl;
        return this;
    }

    /* End of Image Methods */


    public Optional<LocLinkEntity> getMainLink() {
        return Optional.ofNullable(this.mainLink);
    }

    public LocationBuilder withMainLink(LocLinkEntity locLink) {
        // TODO: This is SVR-36 too
        this.mainLink = locLink;
        return this;
    }

    public LocationBuilder withLocLink(LocLink locLink) {
        // TODO: This is SVR-36 too
        this.mainLink = LocLinkEntity.builder()
                .withId(locLink.getId())
                .withLink(locLink.getLink());
        return this;
    }

    public List<PuzzleBuilder> getPuzzleBuilders() {
        return puzzleBuilders;
    }

    public LocationBuilder withPuzzleBuilders(List<PuzzleBuilder> puzzleBuilders) {
        this.puzzleBuilders = puzzleBuilders;
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
                // TODO: SVR-36
//                .withLocationTypeId(locationBuilder.locationTypeId)
                .withLocationGroupId(locationBuilder.locationGroupId)
        ;
    }

}
