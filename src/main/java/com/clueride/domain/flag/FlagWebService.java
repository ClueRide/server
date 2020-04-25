package com.clueride.domain.flag;

import com.clueride.auth.Secured;

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

}
