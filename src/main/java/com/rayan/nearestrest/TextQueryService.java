package com.rayan.nearestrest;

import com.rayan.nearestrest.dto.PlacesSearchTextRequest;
import com.rayan.nearestrest.dto.PlacesSearchTextResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class TextQueryService {

    @ConfigProperty(name = "api.key")
    private String apiKey;
    @Inject
    @RestClient
    GooglePlacesClient googlePlacesClient;


    public PlacesSearchTextResponse searchRestaurants(String query, double lat, double lng) {
        PlacesSearchTextRequest req = new PlacesSearchTextRequest();
        req.textQuery = query;
        req.rankPreference = "DISTANCE";
        req.maxResultCount = 20;

        // location bias
        PlacesSearchTextRequest.Center center = new PlacesSearchTextRequest.Center();
        center.latitude = lat;
        center.longitude = lng;

        PlacesSearchTextRequest.Circle circle = new PlacesSearchTextRequest.Circle();
        circle.center = center;
        circle.radius = 5000.0;

        PlacesSearchTextRequest.LocationBias bias = new PlacesSearchTextRequest.LocationBias();
        bias.circle = circle;

        req.locationBias = bias;

        String fieldMask = "places.displayName,places.rating,places.userRatingCount,places.priceLevel,places.formattedAddress,places.types";

        return googlePlacesClient.searchPlaces(req, apiKey, fieldMask);
    }
}
