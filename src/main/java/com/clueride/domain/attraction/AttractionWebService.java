package com.clueride.domain.attraction;

import com.clueride.auth.Secured;
import com.clueride.domain.location.Location;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * REST API for {@link Location} instances
 */
@Path("/attraction")
public class AttractionWebService {
    @Inject
    private AttractionService attractionService;

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Attraction getById(@QueryParam("id") Integer attractionId) {
        return attractionService.getById(attractionId);
    }

    @POST
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Path("suggest")
    public List<Attraction> getByNameFragment(NameFragmentQuery nameFragmentQuery) {
        return attractionService.getByNameFragment(nameFragmentQuery);
    }

}
