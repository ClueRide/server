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
 * Created by jett on 5/5/20.
 */
package com.clueride.domain.attraction;

import com.clueride.domain.flag.Flag;
import com.clueride.domain.image.ImageLinkEntity;
import com.clueride.domain.location.ReadinessLevel;
import com.clueride.domain.location.latlon.LatLonEntity;
import com.clueride.domain.location.loclink.LocLinkEntity;
import com.clueride.domain.puzzle.PuzzleEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Knows how to assemble the parts of a Attraction.
 *
 * In this incarnation, the target client is the Player application.
 * We may get mileage out of this for the Ranger, but that's
 * not the focus at this date (Jan 2019).
 *
 * OK, on Feb 5 2019, we're looking at whether this will serve the
 * Ranger app ;-). Looks like the Puzzle Builders are considered to
 * determine the ReadinessLevel of the Attraction. I think we can consult
 * the Puzzle Service to find what we need.
 */
@Entity
@Table(name="location")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttractionEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="location_pk_sequence")
    @SequenceGenerator(name="location_pk_sequence",sequenceName="location_id_seq", allocationSize=1)
    private Integer id;

    /* Human readable characteristics. */
    private String name;
    private String description;

    @Column(name="node_id") private Integer nodeId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="featured_image_id")
    private ImageLinkEntity featuredImage;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "main_link_id")
    private LocLinkEntity mainLink;

    @Column(name="location_type_id", updatable = false, insertable = false)
    private Integer locationTypeId;

    @Column(name="location_group_id") private Integer locationGroupId;

    @Transient
    private LatLonEntity latLonEntity;
    @Transient
    private URL featuredImageUrl;
    @Transient
    private Integer featuredImageId;
    @Transient
    private List<PuzzleEntity> puzzleEntities;
    @Transient
    private Integer googlePlaceId;

    @Transient
    private Integer establishmentId;

    @Transient
    private Map<String, Optional<Double>> tagScores = new HashMap<>();

    @Transient
    private String establishment;

    @Transient
    private ReadinessLevel readinessLevel;

    @Transient
    private List<Flag> flags;

    public AttractionEntity() {}

    public static com.clueride.domain.attraction.AttractionEntity builder() {
        return new com.clueride.domain.attraction.AttractionEntity();
    }

    public static AttractionEntity from(Attraction location) {
        return builder()
                .withId(location.getId())
                .withName(location.getName())
                .withDescription(location.getDescription())
                .withLocationTypeId(location.getLocationTypeId())
                .withNodeId(location.getNodeId())
                .withLatLon(location.getLatLon())
                .withFeaturedImage(ImageLinkEntity.from(location.getFeaturedImage()))
                .withEstablishmentId(location.getEstablishment())
                .withTagScores(location.getTagScores())
                ;
    }

    public Attraction build() {
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
        return new Attraction(this);
    }

    public AttractionEntity withId(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * If the value has been set (by reading from the AttractionStore), use that
     * value, otherwise, we're creating a new instance the and idProvider should
     * give us the next available value.
     * @return Unique ID for this instance of Attraction.
     */
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AttractionEntity withName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AttractionEntity withDescription(String description) {
        this.description = description;
        return this;
    }

    /* Location Type ID. */
    public Integer getLocationTypeId() {
        return locationTypeId;
    }

    /**
     * Service is responsible for taking this value and looking up the
     * LocationType(Entity) via its service.
     *
     * @param locationTypeId chosen from a list of possible Location Types.
     * @return this.
     */
    public AttractionEntity withLocationTypeId(Integer locationTypeId) {
        this.locationTypeId = locationTypeId;
        return this;
    }

    /* TODO CI-152: Flagging Attraction; expect to play with ReadinessLevel. */

    public ReadinessLevel getReadinessLevel() {
        return readinessLevel;
    }

    public AttractionEntity withReadinessLevel(ReadinessLevel readinessLevel) {
        this.readinessLevel = readinessLevel;
        return this;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public AttractionEntity withNodeId(Integer nodeId) {
        this.nodeId = nodeId;
        return this;
    }
    /* FUTURE */

    public Map<String, Optional<Double>> getTagScores() {
        return tagScores;
    }

    public AttractionEntity withTagScores(Map<String, Optional<Double>> tagScores) {
        this.tagScores = tagScores;
        return this;
    }

    public Integer getLocationGroupId() {
        return locationGroupId;
    }

    public AttractionEntity withLocationGroupId(Integer locationGroupId) {
        this.locationGroupId = locationGroupId;
        return this;
    }

    public Integer getEstablishmentId() {
        return establishmentId;
    }

    public AttractionEntity withEstablishmentId(Integer establishmentId) {
        this.establishmentId = establishmentId;
        return this;
    }

    public String getEstablishment() {
        return establishment;
    }

    public AttractionEntity withEstablishment(String establishment) {
        this.establishment = establishment;
        return this;
    }

    public LatLonEntity getLatLonEntity() {
        return latLonEntity;
    }

    public AttractionEntity withLatLon(LatLonEntity latLonEntity) {
        this.latLonEntity = latLonEntity;
        if (latLonEntity != null) {
            this.nodeId = latLonEntity.getId();
        }
        return this;
    }
    /* Image Methods */

    public ImageLinkEntity getFeaturedImage() {
        return featuredImage;
    }

    public AttractionEntity withFeaturedImage(ImageLinkEntity featuredImage) {
        this.featuredImage = featuredImage;
        this.featuredImageId = featuredImage.getId();
        try {
            this.featuredImageUrl = new URL(featuredImage.getUrl());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Bad URL: " + featuredImage.getUrl(), e);
        }
        return this;
    }

    public AttractionEntity clearFeaturedImage() {
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

    public AttractionEntity withFeaturedImageId(int imageId) {
        this.featuredImageId = imageId;
        return this;
    }

    public AttractionEntity withFeaturedImageUrl(URL featuredImageUrl) {
        this.featuredImageUrl = featuredImageUrl;
        return this;
    }

    /* End of Image Methods */


    public Optional<LocLinkEntity> getMainLink() {
        return Optional.ofNullable(this.mainLink);
    }

    public AttractionEntity withMainLink(LocLinkEntity locLink) {
        // TODO: SVR-94
        this.mainLink = locLink;
        return this;
    }

    public AttractionEntity withLocLink(LocLinkEntity locLinkEntity) {
        // TODO: SVR-94
        this.mainLink = locLinkEntity;
        return this;
    }

    public List<PuzzleEntity> getPuzzleEntities() {
        // TODO SVR-94
        return puzzleEntities;
    }

    public AttractionEntity withPuzzleBuilders(List<PuzzleEntity> puzzleEntities) {
        // TODO SVR-94
        this.puzzleEntities = puzzleEntities;
        return this;
    }

    public Integer getGooglePlaceId() {
        return googlePlaceId;
    }
    /* FUTURE */

    public AttractionEntity withGooglePlaceId(Integer googlePlaceId) {
        this.googlePlaceId = googlePlaceId;
        return this;
    }

    public List<Flag> getFlags() {
        return flags;
    }

    public AttractionEntity withFlags(List<Flag> flags) {
        this.flags = flags;
        return this;
    }
}
