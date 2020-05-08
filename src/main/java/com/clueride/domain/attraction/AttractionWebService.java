package com.clueride.domain.attraction;

import com.clueride.auth.Secured;
import com.clueride.domain.location.Location;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * REST API for {@link Location} instances
 */
@Path("attraction")
public class AttractionWebService {
    @Inject
    private AttractionService attractionService;

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Attraction getById(@QueryParam("id") Integer attractionId) {
        return attractionService.getById(attractionId);
    }

}
