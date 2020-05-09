package com.clueride.domain.flag;

import com.clueride.auth.Secured;
import com.clueride.domain.flag.reason.FlagReason;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/flag")
public class FlagWebService {
    @Inject
    private FlagService flagService;

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public List<Flag> getAllFlags() {
        return flagService.getAllFlags();
    }

    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Flag addNewFlag(FlagEntity flagEntity) {
        return flagService.addNewFlag(flagEntity);
    }

    @GET
    @Secured
    @Path("course/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Flag> getFlagsForCourse(@PathParam("id") Integer courseId) {
        return flagService.getFlagsForCourse(courseId);
    }

    @GET
    @Secured
    @Path("reason")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FlagReason> getFlagReasons() {
        return flagService.getReasons();
    }

}
