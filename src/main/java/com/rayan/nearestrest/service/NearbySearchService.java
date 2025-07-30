package com.rayan.nearestrest.service;

import com.rayan.nearestrest.clinet.GooglePlacesClientNearbySearch;
import com.rayan.nearestrest.core.exception.ResultNotFoundException;
import com.rayan.nearestrest.dto.*;
import com.rayan.nearestrest.dto.nearbySearch.LocationRestriction;
import com.rayan.nearestrest.dto.request.PlacesNearbySearchRequest;
import com.rayan.nearestrest.dto.places.Place;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.rayan.nearestrest.core.enumeration.PriceLevel.PRICE_LEVEL_UNKNOWN;

@ApplicationScoped
public class NearbySearchService {

    private final String[] includedTypesHamburger = {"hamburger_restaurant", "american_restaurant"};
    private final String[] includedTypesCoffee = {"coffee_shop", "cafe"};
    private final String[] includedTypesIndianFood = {"indian_restaurant"};
    private final String[] includedTypesDefault = {""};
    private final String[] excludedTypesHamburger = {"pizza_restaurant", "middle_eastern_restaurant", "seafood_restaurant"};
    private final String[] excludedTypesCoffee = {"tea_house"};
    private final String fieldMask = "places.displayName,places.rating,places.userRatingCount,places.priceLevel,places.formattedAddress,places.types,places.googleMapsUri,places.priceLevel";
    @ConfigProperty(name = "api.key")
    private String apiKey;

    @Inject
    @RestClient
    GooglePlacesClientNearbySearch nearbyPlacesClient;

    public RestaurantResult searchRestaurants(double lat, double lng, String type) {
        PlacesNearbySearchRequest req = constructRequest(lat, lng, type);
        PlacesTextSearchResponse res = nearbyPlacesClient.searchPlaces(req, apiKey, fieldMask);
        if (res.getPlaces() == null) {
            throw new ResultNotFoundException("No results were returned from Google Places API");
        }

        List<Place> places = new ArrayList<>();
        if(type.contains("hamburger")) {
            places = excludeChainRestaurants(res.getPlaces());
        } else if(type.contains("coffee")) {
            places = excludeCoffees(res.getPlaces());
        } else {
            places = res.getPlaces();
        }
        List<Place> topFive = getTop10Places(places);
        List<RestaurantResultDTO> resultDTOs = parseRestaurants(topFive);
        System.out.println(resultDTOs + "=============");
        return new RestaurantResult(resultDTOs);
    }


    // Helpers methods

    public List<Place> getTop10Places(List<Place> places) {
        return places.stream()
                .filter(p -> p.getRating() != null && p.getUserRatingCount() != null)
                .sorted((p1, p2) -> Double.compare(
                        calculateScore(p2), // descending
                        calculateScore(p1)
                ))
                .limit(10)
                .collect(Collectors.toList());
    }

    private double calculateScore(Place place) {
        double rating = place.getRating();
        int count = place.getUserRatingCount();
        return (count > 0) ? rating * Math.log10(count) : 0;
    }

    private List<Place> excludeChainRestaurants(List<Place> places) {
        List<String> chainRestaurants = List.of("McDonald", "KFC", "Burger King", "Kudu", "Shawarma House", "Hardee's", "Burgerizzr","وهمي","Hamburgini");

        return places.stream()
                .filter(place ->
                        place.getDisplayName() != null &&
                                place.getDisplayName().getText() != null &&
                                chainRestaurants.stream().noneMatch(name ->
                                        place.getDisplayName().getText().toLowerCase().contains(name.toLowerCase())
                                )
                )
                .collect(Collectors.toList());
    }

    private List<Place> excludeCoffees(List<Place> places) {
        List<String> chainRestaurants = List.of("dunkin", "Half Million","Starbucks","ABYAT");

        return places.stream()
                .filter(place ->
                        place.getDisplayName() != null &&
                                place.getDisplayName().getText() != null &&
                                chainRestaurants.stream().noneMatch(name ->
                                        place.getDisplayName().getText().toLowerCase().contains(name.toLowerCase())
                                )
                )
                .collect(Collectors.toList());
    }

    // Mapper
    private List<RestaurantResultDTO> parseRestaurants(List<Place> topFive) {
        return topFive.stream()
                .map(place -> new RestaurantResultDTO(
                        place.getDisplayName() != null ? place.getDisplayName().getText() : "Unknown",
                        place.getRating() != null ? place.getRating() : 0.0,
                        place.getUserRatingCount() != null ? place.getUserRatingCount() : 0,
                        place.getGoogleMapsUri(),
                        place.getPriceLevel() != null ? place.getPriceLevel() : PRICE_LEVEL_UNKNOWN
                ))
                .collect(Collectors.toList());
    }

    private PlacesNearbySearchRequest constructRequest(double lat, double lng, String type) {
        PlacesNearbySearchRequest req = new PlacesNearbySearchRequest();
        req.setRankPreference("POPULARITY");
        req.setMaxResultCount(20);

        req.setIncludedTypes(setRestaurantType(type));
        req.setExcludedTypes(setExcludedTypes(type));

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
        return req;
    }

    private String[] setRestaurantType(String type) {

        if (type.contains("coffee")) {
            return includedTypesCoffee;
        } else if (type.equalsIgnoreCase("indian")) {
            return includedTypesIndianFood;
        } else if (type.contains("hamburger")) {
            return includedTypesHamburger;
        }
        // Default types
        return includedTypesDefault;
    }

    private String[] setExcludedTypes(String type) {

        if (type.contains("coffee")) {
            return excludedTypesCoffee;
        } else if (type.contains("hamburger")) {
            return excludedTypesHamburger;
        }
        // Default types
        return new String [0];
    }
}