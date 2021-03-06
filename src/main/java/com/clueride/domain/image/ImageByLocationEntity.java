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
 * Created by jett on 2/10/19.
 */
package com.clueride.domain.image;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.clueride.domain.location.Location;

/**
 * Entity representing the relationship between a {@link Location} instance and
 * its set of {@link ImageLinkEntity}.
 */
@Entity
@Table(name="image_by_location")
public class ImageByLocationEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="image_by_location_pk_sequence")
    @SequenceGenerator(name="image_by_location_pk_sequence",
            sequenceName="images_by_location_id_seq",
            allocationSize=1)
    private Integer id;

    @Column(name="image_id") private Integer imageId;
    @Column(name="location_id") private Integer locationId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

}
