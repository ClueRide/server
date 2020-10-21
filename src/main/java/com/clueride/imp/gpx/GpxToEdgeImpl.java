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
 * Created by jett on 3/17/19.
 */
package com.clueride.imp.gpx;

import com.clueride.network.edge.EdgeEntity;
import org.geolatte.geom.*;
import org.geolatte.geom.crs.CoordinateReferenceSystems;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Implementation of {@link GpxToEdge}.
 *
 * This parses the raw file into a Track which holds the name and source of the file as well as the ordered
 * list of 3D points.
 */
public class GpxToEdgeImpl implements GpxToEdge {

    @Override
    public EdgeEntity edgeFromGpxFile(File gpxFile) {
        EdgeEntity edgeBuilder = new EdgeEntity();

        Track track = trackFromFile(gpxFile);
        LineString<Position> lineString = lineStringFromTrack(track);

        return edgeBuilder
                .withName(track.getDisplayName())
                .withTrackReference(track.getSourceUrl())
                .withPoints(lineString)
        ;
    }

    @Override
    public EdgeEntity edgeFromGpxStream(InputStream gpxStream) {
        EdgeEntity edgeEntity = EdgeEntity.builder();

        Track track = trackFromStream(gpxStream);
        LineString<Position> lineString = lineStringFromTrack(track);

        return edgeEntity
                .withName(track.getDisplayName())
                .withTrackReference(track.getSourceUrl())
                .withPoints(lineString);
    }

    /**
     * Opens the file, reads it into a String and then uses ParseGPX to parse the file into a Track.
     *
     * @param gpxFile {@link File} containing the raw GPX data.
     * @return Track representation of the file's contents.
     */
    private Track trackFromFile(File gpxFile) {
        String stringBuffer = "";
        try {
            stringBuffer = new String(
                    Files.readAllBytes(
                            Paths.get(gpxFile.toURI())
                    )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        return (new ParseGPX()).getTrackFromGPX(stringBuffer);
    }

    private Track trackFromStream(InputStream gpxStream) {
        String stringBuffer = "";
        stringBuffer = new BufferedReader(
                new InputStreamReader(gpxStream, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n")
                );
        return (new ParseGPX()).getTrackFromGPX(stringBuffer);
    }

    private LineString<Position> lineStringFromTrack(Track track) {
        PositionSequenceBuilder positionSequenceBuilder = PositionSequenceBuilders.fixedSized(
                track.getTrackpoints().size(),
                C3D.class
        );

        for (Trackpoint trackPoint : track.getTrackpoints()) {
            positionSequenceBuilder.add(
                    new C3D(
                            trackPoint.getLonDouble(),
                            trackPoint.getLatDouble(),
                            trackPoint.getAltitude()
                    )
            );
        }

        return new LineString(
                positionSequenceBuilder.toPositionSequence(),
                CoordinateReferenceSystems.PROJECTED_3D_METER
        );
    }

}
