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
package com.clueride.network.edge;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.geolatte.geom.LineString;
import org.geolatte.geom.Position;

/**
 * Persistable Builder for {@link Edge} instances.
 */
@Entity(name = "edge")
public class EdgeBuilder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "edge_pk_sequence")
    @SequenceGenerator(name = "edge_pk_sequence", sequenceName = "edge_id_seq", allocationSize = 1)
    private int id;
    @Column(name = "original_edge_id")
    private int originalEdgeId;
    @Column(name = "name")
    private String name;
    @Column(name = "track_reference")
    private String trackReference;
    @Column(name = "points")
    private LineString<Position> points;

    public EdgeBuilder() {
    }

    public static EdgeBuilder builder() {
        return new EdgeBuilder();
    }

    public Edge build() {
        return new Edge(this);
    }

    public int getId() {
        return id;
    }

    public EdgeBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public int getOriginalEdgeId() {
        return originalEdgeId;
    }

    public EdgeBuilder withOriginalEdgeId(int originalEdgeId) {
        this.originalEdgeId = originalEdgeId;
        return this;
    }

    public String getName() {
        return name;
    }

    public EdgeBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public String getTrackReference() {
        return trackReference;
    }

    public EdgeBuilder withTrackReference(String trackReference) {
        this.trackReference = trackReference;
        return this;
    }

    public LineString<Position> getPoints() {
        return points;
    }

    public EdgeBuilder withPoints(LineString<Position> points) {
        this.points = points;
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
