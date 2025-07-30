package com.rayan.nearestrest.resource;

import com.rayan.nearestrest.dto.RestaurantResult;
import com.rayan.nearestrest.service.NearbySearchService;
import com.rayan.nearestrest.service.TextQueryService;
import com.rayan.nearestrest.dto.PlacesTextSearchResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/v1/rest")
public class TextQueryResource {

    @Inject
    TextQueryService textQueryService;
    @Inject
    NearbySearchService nearbySearchService;

    @GET
    @Path("/textSearch")
    public PlacesTextSearchResponse getTextSearch(
            @QueryParam("lat") double lat,
            @QueryParam("lng") double lng,
            @QueryParam("query") @DefaultValue("rice restaurant") String query
    ) {
        return textQueryService.searchRestaurants(query, lat, lng);
    }


    @GET
    @Path("/nearbySearch")
    public RestaurantResult getNearbySearch(
            @QueryParam("lat") double lat,
            @QueryParam("lng") double lng,
            @QueryParam("type") String type
    ) {
        return nearbySearchService.searchRestaurants(lat, lng, type);
    }
}
