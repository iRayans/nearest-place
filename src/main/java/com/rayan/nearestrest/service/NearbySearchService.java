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
import org.jboss.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.rayan.nearestrest.core.enumeration.PriceLevel.PRICE_LEVEL_UNKNOWN;

@ApplicationScoped
public class NearbySearchService {

    private static final Logger LOGGER = Logger.getLogger(NearbySearchService.class);

    private final String[] includedTypesDefault = {"restaurant"};
    private final String[] includedTypesHamburger = {"hamburger_restaurant", "american_restaurant"};
    private final String[] includedTypesCoffee = {"coffee_shop", "cafe"};
    private final String[] includedTypesIndianFood = {"indian_restaurant"};
    private final String[] excludedTypesHamburger = {"pizza_restaurant", "middle_eastern_restaurant", "seafood_restaurant"};
    private final String[] excludedTypesCoffee = {"tea_house"};
    private final String fieldMask = "places.displayName,places.rating,places.userRatingCount,places.priceLevel,places.formattedAddress,places.types,places.googleMapsUri,places.priceLevel";
    @ConfigProperty(name = "api.key")
    private String apiKey;

    @Inject
    @RestClient
    GooglePlacesClientNearbySearch nearbyPlacesClient;

    public RestaurantResult searchRestaurants(double lat, double lng, String type) {
        LOGGER.info("Searching restaurants for " + type + " at " + lat + ", " + lng);
        PlacesNearbySearchRequest req = constructRequest(lat, lng, type);
        PlacesTextSearchResponse res = nearbyPlacesClient.searchPlaces(req, apiKey, fieldMask);
        if (res.getPlaces() == null) {
            LOGGER.error("Failed to search restaurants for " + type);
            throw new ResultNotFoundException("No results were returned from Google Places API");
        }

        List<Place> places;
        if (type.contains("hamburger")) {
            places = excludeChainRestaurants(res.getPlaces());
        } else if (type.contains("coffee")) {
            places = excludeCoffees(res.getPlaces());
        } else {
            places = res.getPlaces();
        }
        List<Place> topFive = getTop10Places(places);
        List<RestaurantResultDTO> resultDTOs = parseRestaurants(topFive);
        return new RestaurantResult(resultDTOs);
    }


    // Helpers methods

    public List<Place> getTop10Places(List<Place> places) {
        return places.stream()
                // Filter out places with missing ratings
                .filter(p -> p.getRating() != null && p.getUserRatingCount() != null)
                // Sort the remaining places by calculated score (highest first)
                .sorted((p1, p2) -> Double.compare(
                        calculateScore(p2),
                        calculateScore(p1)
                ))
                // Take only the top 10
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * Calculates a score for a place using its rating and number of user reviews.
     * <p>
     * The score helps us rank places by combining:
     * - Rating (higher is better)
     * - Number of reviews (more reviews = more trusted)
     * <p>
     * We use log10 of the review count to reduce the impact of very large numbers.
     * This way, a place with 5.0 stars from 10 people won't beat a place with 4.8 stars from 5000 people.
     *
     * @param place the place to calculate the score for
     * @return the calculated score (higher means better)
     */
    private double calculateScore(Place place) {
        double rating = place.getRating();
        int count = place.getUserRatingCount();
        return (count > 0) ? rating * Math.log10(count) : 0;
    }


    private List<Place> excludeChainRestaurants(List<Place> places) {
        List<String> chainRestaurants = List.of("McDonald", "KFC", "Burger King", "Kudu", "Shawarma House", "Hardee's", "Burgerizzr", "وهمي", "Hamburgini");

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
        List<String> chainRestaurants = List.of("dunkin", "Half Million", "Starbucks", "ABYAT");

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
    // TODO: Move it to a Mapper class.
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
        LOGGER.info("Constructing request for " + type + " at " + lat + ", " + lng);
        PlacesNearbySearchRequest req = new PlacesNearbySearchRequest();
        req.setRankPreference("POPULARITY");
        req.setMaxResultCount(20);

        if (type == null) {
            // if 'type' for some reason is null, set it to 'Hamburger'.
            type = "restaurant";
        }
        req.setIncludedTypes(setRestaurantType(type));
        if (!type.equals("restaurant")) {
            req.setExcludedTypes(setExcludedTypes(type));
        }
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
        LOGGER.info("Setting restaurant type to " + type);
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
        // return excludedTypesHamburger for now.
        return excludedTypesHamburger;
    }
}