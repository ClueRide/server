package com.clueride.domain.path;

import com.clueride.auth.Secured;
import com.clueride.domain.course.CourseEntity;
import com.clueride.domain.path.meta.PathMeta;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * REST API for "LinkPath" representations of {@link Path} instances.
 *
 * These instances are tuples recording the relationship between
 * Course, Departure Attraction, Destination Attraction, and the
 * ordered list of Edges.
 *
 * Lists of Paths are ordered as well. They are related to the ordered
 * list of Attractions such that most Attractions will be the end of
 * one path and the start of the next path.
 */
@Path("/path")
public class PathWebService {

    @Inject
    private PathService pathService;

    @POST
    @Secured
    @Path("link")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PathMeta> getLinkPathsForAttractions(CourseEntity courseEntity) {
        return pathService.getLinkPathsForAttractions(
                courseEntity.getId(),
                courseEntity.getLocationIds()
        );
    }

}
