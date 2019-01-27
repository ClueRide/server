/**
 *   Copyright 2015 Jett Marks
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created Oct 20, 2015
 */
package com.clueride.domain.path;

import java.util.List;

import com.clueride.domain.course.Course;
import com.clueride.domain.course.Step;
import com.clueride.domain.location.Location;

/**
 * Represents a particular choice of Segments to travel between two
 * {@link Location}s.
 *
 * Also holds
 * <ul>
 *     <li>List of Edge IDs</li>
 *     <li>Node ID of Departure</li>
 *     <li>Node ID of Destination</li>
 * </ul>
 *
 * @author jett
 */
public interface Path extends Step {
    // TODO: Unsure who is using this, but commented out to avoid bringing this code in right now.
//    SortedSet<Segment> getSegments();

    /**
     * Uniquely identifies this particular sequence of segments/edges between
     * two nodes.
     * @return Integer representing this Path.
     */
    Integer getId();

    /**
     * Since a given Path can be part of multiple courses, this may
     * not always be defined. As part of a given Course however, this
     * field will reference that {@link Course}.
     * @return associated Course, if any and null otherwise.
     */
    Integer getCourseId();

    /**
     * ID of the Node at the start of this Path; the Departure.
     * @return Integer representing the point of departure.
     */
    Integer getStartNodeId();

    /**
     * ID of the Node at the end of this Path; the Destination.
     * @return Integer representing the destination point.
     */
    Integer getEndNodeId();

    /**
     * Retrieves ordered List of IDs for the Edges.
     * @return ordered List of Edges.
     */
    List<Integer> getEdgeIds();

    /**
     * ID of the departure Location.
     * @return unique Integer representing the Departure Location.
     */
    Integer getStartLocationId();

    /**
     * ID of the Destination Location.
     * @return unique Integer representing the Destination Location.
     */
    Integer getEndLocationId();

}
