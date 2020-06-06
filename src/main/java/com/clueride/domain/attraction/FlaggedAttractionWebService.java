package com.clueride.domain.attraction;

import com.clueride.auth.Secured;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("flagged-attraction")
public class FlaggedAttractionWebService {

    @Inject
    private FlaggedAttractionService flaggedAttractionService;

    @GET
    @Secured
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Attraction getByIdWithFlags(@PathParam("id") Integer attractionId) {
        return flaggedAttractionService.getByIdWithFlags(attractionId);
    }

}