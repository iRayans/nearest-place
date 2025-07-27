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

@ApplicationScoped
public class NearbySearchService {

    @ConfigProperty(name = "api.key")
    private String apiKey;

    @Inject
    @RestClient
    GooglePlacesClientNearbySearch nearbyPlacesClient;

    public RestaurantResult searchRestaurants(double lat, double lng) {
        PlacesNearbySearchRequest req = new PlacesNearbySearchRequest();
        req.setRankPreference("POPULARITY");
        req.setMaxResultCount(20);
        String[] includedTypes = {"hamburger_restaurant","american_restaurant"};
        String[] excludedTypes = {"pizza_restaurant","middle_eastern_restaurant","seafood_restaurant"};

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
        // Better to extract them into ENUM class.
        String fieldMask = "places.displayName,places.rating,places.userRatingCount,places.priceLevel,places.formattedAddress,places.types,places.googleMapsUri,places.priceLevel";
        PlacesTextSearchResponse res = nearbyPlacesClient.searchPlaces(req, apiKey, fieldMask);
        List<Place> places = res.getPlaces();
        List<Place> excludedPlaces = excludeChainRestaurants(places);
        List<Place> topFive = getTop10Places(excludedPlaces);

        System.out.println("======================================================");
        AtomicInteger counter = new AtomicInteger(1);
        topFive.stream()
                .forEach(place -> {
                    String name = place.getDisplayName() != null ? place.getDisplayName().getText() : "Unknown";
                    Double rating = place.getRating();
                    System.out.println(counter + "-" +"Name: " + name + ", Rating: " + rating + " People Rated Count: " + place.getUserRatingCount());
                    counter.getAndIncrement();
                });

        // Extract it to a method or create a mapper.
        List<RestaurantResultDTO> resultDTOs = topFive.stream()
                .map(place -> new RestaurantResultDTO(
                        place.getDisplayName() != null ? place.getDisplayName().getText() : "Unknown",
                        place.getRating() != null ? place.getRating() : 0.0,
                        place.getUserRatingCount() != null ? place.getUserRatingCount() : 0,
                        place.getGoogleMapsUri(),
                        parsePriceLevel(place.getPriceLevel().name())
                ))
                .collect(Collectors.toList());

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


    public double calculateScore(Place place) {
        double rating = place.getRating();
        int count = place.getUserRatingCount();
        return (count > 0) ? rating * Math.log10(count) : 0;
    }

    public List<Place> excludeChainRestaurants(List<Place> places) {
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
            return value != null ? PriceLevel.valueOf(value) : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }


}
