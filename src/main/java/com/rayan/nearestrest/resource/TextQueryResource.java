package com.rayan.nearestrest.resource;

import com.rayan.nearestrest.TextQueryService;
import com.rayan.nearestrest.dto.PlacesSearchTextResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/v1/rest")
public class TextQueryResource {

    @Inject
    TextQueryService textQueryService;

    @GET
    @Path("/search")
    public PlacesSearchTextResponse getNearby(
            @QueryParam("lat") double lat,
            @QueryParam("lng") double lng,
            @QueryParam("query") @DefaultValue("burger restaurants") String query
    ) {
        return textQueryService.searchRestaurants(query, lat, lng);
    }
}
