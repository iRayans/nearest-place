package com.rayan.nearestrest.service;

import com.rayan.nearestrest.clinet.GooglePlacesClientNearbySearch;
import com.rayan.nearestrest.dto.nearbySearch.LocationRestriction;
import com.rayan.nearestrest.dto.nearbySearch.PlacesNearbySearchRequest;
import com.rayan.nearestrest.dto.textSearch.Center;
import com.rayan.nearestrest.dto.textSearch.Circle;
import com.rayan.nearestrest.dto.PlacesTextSearchResponse;
import com.rayan.nearestrest.dto.places.Place;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class NearbySearchService {

    @ConfigProperty(name = "api.key")
    private String apiKey;

    @Inject
    @RestClient
    GooglePlacesClientNearbySearch nearbyPlacesClient;


    public PlacesTextSearchResponse searchRestaurants(double lat, double lng) {
        PlacesNearbySearchRequest req = new PlacesNearbySearchRequest();
        req.setRankPreference("POPULARITY");
        req.setMaxResultCount(20);
        String[] includedTypes = {"hamburger_restaurant","american_restaurant"};
        req.setIncludedTypes(includedTypes);

        // location
        Center center = new Center();
        center.setLatitude(lat);
        center.setLongitude(lng);

        Circle circle = new Circle();
        circle.setCenter(center);
        circle.setRadius(5000.0); // hardcoded for now.

        LocationRestriction locationRestriction = new LocationRestriction();
        locationRestriction.setCircle(circle);

        req.setLocationRestriction(locationRestriction);
        // Better to extract them into ENUM class.
        String fieldMask = "places.displayName,places.rating,places.userRatingCount,places.priceLevel,places.formattedAddress,places.types,places.googleMapsUri";
        System.out.println(req);
        PlacesTextSearchResponse res = nearbyPlacesClient.searchPlaces(req, apiKey, fieldMask);

        for (Place place : res.getPlaces()) {
            System.out.println(place);
        }

        return res;
    }
}
