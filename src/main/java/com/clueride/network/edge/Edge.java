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
 * Created by jett on 1/12/19.
 */
package com.clueride.network.edge;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.geolatte.geom.LineString;

/**
 * Represents a record in the Edge table holding a Geometry (LINESTRING)
 * representing the connection between two Location Nodes.
 *
 * The @JsonIgnore avoids Jackson problems converting the Geometry field
 * (LineString in this case) into JSON.  See the comment on ticket CA-417
 * for more detail.
 */
@Immutable
public class Edge {
    private int id;
    private String name;
    private String trackReference;
    @JsonIgnore private LineString points;
    private String lineString;

    /**
     * Default constructor accepts Builder instance.
     * @param builder fully-populated mutable instance.
     */
    Edge(EdgeBuilder builder) {
        this.id = builder.getId();
        this.name = builder.getName();
        this.trackReference = builder.getTrackReference();
        this.points = builder.getPoints();
        this.lineString = points.toString();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTrackReference() {
        return trackReference;
    }

    public LineString getPoints() {
        return points;
    }

    public String getLineString() {
        return lineString;
    }
}
