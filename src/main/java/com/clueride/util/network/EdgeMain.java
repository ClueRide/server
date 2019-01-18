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
package com.clueride.util.network;

import java.lang.invoke.MethodHandles;
import java.util.List;

import com.vividsolutions.jts.io.WKTWriter;
import org.geolatte.geom.LineString;
import org.geolatte.geom.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clueride.network.edge.EdgeBuilder;
import com.clueride.util.network.edge.EdgeStoreJson;
import com.clueride.util.network.edge.JsonBasedEdge;
import static org.geolatte.geom.codec.Wkt.fromWkt;

/**
 * Utility to read and persist Edges from GeoJSON into the
 * spatially-enabled database.
 */
public class EdgeMain {
    private static Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public static void main(String[] args) {
        WKTWriter wktWriter = new WKTWriter(3);
        EdgeStoreJson edgeStore = new EdgeStoreJson();

        List<JsonBasedEdge> jsonBasedEdges = edgeStore.getEdges();
        for (JsonBasedEdge jsonBasedEdge : jsonBasedEdges) {
            /* Turn the JTS LineString into a GeoLatte LineString */
            String wkt = wktWriter.writeFormatted(jsonBasedEdge.getLineString());
            EdgeBuilder edgeBuilder = EdgeBuilder.builder()
                    .withOriginalEdgeId(jsonBasedEdge.getEdgeId())
                    .withName(jsonBasedEdge.getName())
                    .withTrackReference(jsonBasedEdge.getReferenceString())
                    .withPoints((LineString<Position>)fromWkt(wkt));
            LOGGER.debug(edgeBuilder.toString());

        }
    }

}
