package com.rayan.nearestrest.resource;

import com.rayan.nearestrest.dto.RestaurantResult;
import com.rayan.nearestrest.service.NearbySearchService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/v1/rest")
public class NearbySearchResource {

    @Inject
    NearbySearchService nearbySearchService;

    @GET
    @Path("/nearbySearch")
    public RestaurantResult getNearbySearch(
            @QueryParam("lat") double lat,
            @QueryParam("lng") double lng,
            @QueryParam("type") String type
    ) {
        lat = round4(lat);
        lng = round4(lng);
        String TYPE = (type == null ? "restaurant" : type.toLowerCase().trim());
        return nearbySearchService.searchRestaurants(lat, lng, TYPE);
    }

    private static double round4(double v) {
        return Math.round(v * 1e4) / 1e4;
    }

}
