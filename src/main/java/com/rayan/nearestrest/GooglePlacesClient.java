package com.rayan.nearestrest;

import com.rayan.nearestrest.dto.PlacesSearchTextRequest;
import com.rayan.nearestrest.dto.PlacesSearchTextResponse;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/v1/places:searchText")
@RegisterRestClient(configKey = "google-places-api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface GooglePlacesClient {

    @POST
    PlacesSearchTextResponse searchPlaces(PlacesSearchTextRequest request,
                                          @HeaderParam("X-Goog-Api-Key") String apiKey,
                                          @HeaderParam("X-Goog-FieldMask") String fieldMask);
}
