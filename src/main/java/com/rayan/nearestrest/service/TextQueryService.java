package com.rayan.nearestrest.service;

import com.rayan.nearestrest.clinet.GooglePlacesClientTextSearch;
import com.rayan.nearestrest.dto.Center;
import com.rayan.nearestrest.dto.Circle;
import com.rayan.nearestrest.dto.textSearch.LocationBias;
import com.rayan.nearestrest.dto.request.PlacesTextSearchRequest;
import com.rayan.nearestrest.dto.PlacesTextSearchResponse;
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
    GooglePlacesClientTextSearch googlePlacesClient;

    public PlacesTextSearchResponse searchRestaurants(String query, double lat, double lng) {
        PlacesTextSearchRequest req = new PlacesTextSearchRequest();
        req.setTextQuery(query);
        req.setRankPreference("DISTANCE");
        req.setMaxResultCount(20);

        // location bias
        Center center = new Center();
        center.setLatitude(lat);
        center.setLongitude(lng);

        Circle circle = new Circle();
        circle.setCenter(center);
        circle.setRadius(5000.0); // hardcoded for now.

        LocationBias bias = new LocationBias();
        bias.setCircle(circle);

        req.setMinRating(4.0);
        req.setLocationBias(bias);

        // Better to extract them into ENUM class.
        String fieldMask = "places.displayName,places.rating,places.userRatingCount,places.priceLevel,places.formattedAddress,places.types,places.googleMapsUri";

        PlacesTextSearchResponse res = googlePlacesClient.searchPlaces(req, apiKey, fieldMask);

        for (Place place : res.getPlaces()) {
            System.out.println(place);
        }

        return res;
    }
}
