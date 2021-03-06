/*
 * Copyright 2017 Jett Marks
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
 * Created by jett on 9/10/17.
 */
package com.clueride.domain.location.latlon;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Details of a proposed LatLonEntity in support of Locations which may not yet be on the network.
 *
 * DTO at this time.
 */

@XmlRootElement
@Entity
@Table(name="latlon")
public class LatLonEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="latlon_pk_sequence")
    @SequenceGenerator(name="latlon_pk_sequence",sequenceName="node_id_seq", allocationSize=1)
    private Integer id;

    private double lat;
    private double lon;
    private double elev;

    /**
     * Constructor to initialize the two most significant fields.
     * @param lat latitude.
     * @param lon longitude.
     */
    public LatLonEntity(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }
    public LatLonEntity() {
    }

    public int getId() {
        return id;
    }

    public LatLonEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public LatLonEntity setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public LatLonEntity setLon(double lon) {
        this.lon = lon;
        return this;
    }

    public double getElev() {
        return elev;
    }

    public LatLonEntity setElev(double elev) {
        this.elev = elev;
        return this;
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
