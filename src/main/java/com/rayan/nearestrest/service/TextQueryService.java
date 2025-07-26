package com.rayan.nearestrest.service;

import com.rayan.nearestrest.clinet.GooglePlacesClient;
import com.rayan.nearestrest.dto.PlacesSearchTextRequest;
import com.rayan.nearestrest.dto.PlacesSearchTextResponse;
import com.rayan.nearestrest.dto.places.Place;
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
        req.setTextQuery(query);
        req.setRankPreference("DISTANCE");
        req.setMaxResultCount(20);

        // location bias
        PlacesSearchTextRequest.Center center = new PlacesSearchTextRequest.Center();
        center.latitude = lat;
        center.longitude = lng;

        PlacesSearchTextRequest.Circle circle = new PlacesSearchTextRequest.Circle();
        circle.center = center;
        circle.radius = 5000.0; // hardcoded for now.

        PlacesSearchTextRequest.LocationBias bias = new PlacesSearchTextRequest.LocationBias();
        bias.circle = circle;

        req.setLocationBias(bias);

        // Better to extract them into ENUM class.
        String fieldMask = "places.displayName,places.rating,places.userRatingCount,places.priceLevel,places.formattedAddress,places.googleMapsUri";

        PlacesSearchTextResponse res = googlePlacesClient.searchPlaces(req, apiKey, fieldMask);

        for (Place place : res.getPlaces()) {
            System.out.println(place);
        }

        return res;
    }
}
