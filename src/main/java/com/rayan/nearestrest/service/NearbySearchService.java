package com.rayan.nearestrest.service;

import com.rayan.nearestrest.clinet.GooglePlacesClientNearbySearch;
import com.rayan.nearestrest.dto.*;
import com.rayan.nearestrest.dto.nearbySearch.LocationRestriction;
import com.rayan.nearestrest.dto.nearbySearch.PlacesNearbySearchRequest;
import com.rayan.nearestrest.dto.places.Place;
import com.rayan.nearestrest.enumeration.PriceLevel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.rayan.nearestrest.enumeration.PriceLevel.*;

@ApplicationScoped
public class NearbySearchService {

    private final String[]  includedTypes = {"hamburger_restaurant","american_restaurant"};
    private final String[] excludedTypes = {"pizza_restaurant","middle_eastern_restaurant","seafood_restaurant"};
    private final String fieldMask = "places.displayName,places.rating,places.userRatingCount,places.priceLevel,places.formattedAddress,places.types,places.googleMapsUri,places.priceLevel";
    @ConfigProperty(name = "api.key")
    private String apiKey;

    @Inject
    @RestClient
    GooglePlacesClientNearbySearch nearbyPlacesClient;

    public RestaurantResult searchRestaurants(double lat, double lng) {
        PlacesNearbySearchRequest req = constructRequest(lat, lng);
        PlacesTextSearchResponse res = nearbyPlacesClient.searchPlaces(req, apiKey, fieldMask);
        List<Place> places = excludeChainRestaurants(res.getPlaces());
        List<Place> topFive = getTop10Places(places);

        System.out.println("======================================================");
        AtomicInteger counter = new AtomicInteger(1);
        topFive.stream()
                .forEach(place -> {
                    String name = place.getDisplayName() != null ? place.getDisplayName().getText() : "Unknown";
                    Double rating = place.getRating();
                    System.out.println(counter + "-" +"Name: " + name + ", Rating: " + rating + " People Rated Count: " + place.getUserRatingCount() + "PriceLevel: " + place.getPriceLevel());
                    counter.getAndIncrement();
                });

        List<RestaurantResultDTO> resultDTOs = parseRestaurants(topFive);
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
        List<String> chainRestaurants = List.of("McDonald", "KFC", "Burger King","Kudu","Shawarma House","Hardee's","Burgerizzr");

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

    private PriceLevel parsePriceLevel(String value) {
        try {
            return value != null ? valueOf(value) : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // Mapper
    private  List<RestaurantResultDTO> parseRestaurants(List<Place> topFive) {
        return topFive.stream()
                .map(place -> new RestaurantResultDTO(
                        place.getDisplayName() != null ? place.getDisplayName().getText() : "Unknown",
                        place.getRating() != null ? place.getRating() : 0.0,
                        place.getUserRatingCount() != null ? place.getUserRatingCount() : 0,
                        place.getGoogleMapsUri(),
                        parsePriceLevel(place.getPriceLevel() != null ? place.getPriceLevel().name() : PRICE_LEVEL_UNKNOWN.name() )
                ))
                .collect(Collectors.toList());
    }

    private PlacesNearbySearchRequest constructRequest(double lat, double lng) {
        PlacesNearbySearchRequest req = new PlacesNearbySearchRequest();
        req.setRankPreference("POPULARITY");
        req.setMaxResultCount(20);

        req.setIncludedTypes(includedTypes);
        req.setExcludedTypes(excludedTypes);
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

}
