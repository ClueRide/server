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
 * Created by jett on 1/15/19.
 */
package com.clueride.util.network.edge;

import com.vividsolutions.jts.geom.LineString;

/**
 * TODO: Description.
 */
public class JsonBasedEdge {
    private Integer edgeId;
    private String name;
    private String referenceString;
    private LineString lineString;

    public Integer getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(Integer edgeId) {
        this.edgeId = edgeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReferenceString() {
        return referenceString;
    }

    public void setReferenceString(String referenceString) {
        this.referenceString = referenceString;
    }

    public LineString getLineString() {
        return lineString;
    }

    public void setLineString(LineString lineString) {
        this.lineString = lineString;
    }

}
