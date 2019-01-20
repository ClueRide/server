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
 * Created by jett on 1/20/19.
 */
package com.clueride.network.path;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.geolatte.geom.LineString;
import org.geolatte.geom.Position;

/**
 * Represents the Geometry of a Path.
 */
@Entity(name = "PathGeometry")
@Table(name = "path_view")
public class PathGeometry {
    @Id
    @Column(name = "path_id") private int pathId;

    @Column
    private String name;
    @Column(name = "edge_id")
    private int edgeId;
    @Column(name = "points")
    private LineString<Position> points;

    public int getPathId() {
        return pathId;
    }

    public void setPathId(int pathId) {
        this.pathId = pathId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(int edgeId) {
        this.edgeId = edgeId;
    }

    public LineString<Position> getPoints() {
        return points;
    }

    public void setPoints(LineString<Position> points) {
        this.points = points;
    }

}
