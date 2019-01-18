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

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vividsolutions.jts.geom.LineString;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.simple.SimpleFeature;

import com.clueride.util.network.common.JsonSchemaTypeMap;
import com.clueride.util.network.common.JsonStoreLocation;
import com.clueride.util.network.common.JsonStoreType;

/**
 * Knows how to pull the GeoJSON-based Edges into memory.
 */
public class EdgeStoreJson {

    private static final int DIGITS_OF_PRECISION = 5;

    public List<JsonBasedEdge> getEdges() {
        List<JsonBasedEdge> edges = new ArrayList<>();
        File directory = new File(JsonStoreLocation.toString(JsonStoreType.EDGE));
        List<File> files = Arrays.asList(directory.listFiles(new GeoJsonFileFilter()));
        for (File file : files) {
            System.out.println(file.getName());
            SimpleFeature simpleFeature = readFeature(file);
            Integer edgeId = (Integer) simpleFeature.getAttribute("edgeId");
            String name = (String) simpleFeature.getAttribute("name");
            String referenceTrack = (String) simpleFeature.getAttribute("url");
            LineString lineString = (LineString) simpleFeature.getAttribute("the_geom");
            JsonBasedEdge jsonBasedEdge = new JsonBasedEdge();
            jsonBasedEdge.setEdgeId(edgeId);
            jsonBasedEdge.setName(name);
            jsonBasedEdge.setReferenceString(referenceTrack);
            jsonBasedEdge.setLineString(lineString);


            edges.add(jsonBasedEdge);
        }
        return edges;
    }

    SimpleFeature readFeature(File child) {
        SimpleFeature feature = null;
        GeometryJSON geometryJson = new GeometryJSON(DIGITS_OF_PRECISION);
        FeatureJSON featureJson = new FeatureJSON(geometryJson);
        featureJson.setFeatureType(JsonSchemaTypeMap.getSchema(JsonStoreType.EDGE));
        try {
            InputStream iStream = new FileInputStream(child);
            feature = featureJson.readFeature(iStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return feature;
    }

    /**
     * Limit the file names being read to those ending with the extension
     * 'geojson'.
     *
     * Shared locally amongst a few methods.
     *
     * @author jett
     */
    class GeoJsonFileFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return (name.endsWith("geojson"));
        }
    }

}
