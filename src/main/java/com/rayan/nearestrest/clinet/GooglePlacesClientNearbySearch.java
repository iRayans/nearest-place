package com.rayan.nearestrest.clinet;

import com.rayan.nearestrest.dto.nearbySearch.PlacesNearbySearchRequest;
import com.rayan.nearestrest.dto.PlacesTextSearchResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/v1/places:searchNearby")
@RegisterRestClient(configKey = "google-places-api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface GooglePlacesClientNearbySearch {

    @POST
    PlacesTextSearchResponse searchPlaces(PlacesNearbySearchRequest request,
                                          @HeaderParam("X-Goog-Api-Key") String apiKey,
                                          @HeaderParam("X-Goog-FieldMask") String fieldMask);
}
