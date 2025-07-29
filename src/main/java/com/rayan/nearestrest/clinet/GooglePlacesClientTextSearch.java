package com.rayan.nearestrest.clinet;

import com.rayan.nearestrest.dto.request.PlacesTextSearchRequest;
import com.rayan.nearestrest.dto.PlacesTextSearchResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/v1/places:searchText")
@RegisterRestClient(configKey = "google-places-api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface GooglePlacesClientTextSearch {

    @POST
    PlacesTextSearchResponse searchPlaces(PlacesTextSearchRequest request,
                                          @HeaderParam("X-Goog-Api-Key") String apiKey,
                                          @HeaderParam("X-Goog-FieldMask") String fieldMask);
}
