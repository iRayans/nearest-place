package com.rayan.nearestrest.core.helper;

import com.rayan.nearestrest.dto.places.Place;

import java.util.List;
import java.util.stream.Collectors;

public class PlacesHelper {

    private final static String[] includedTypesDefault = {"restaurant"};
    private final static String[] includedTypesHamburger = {"hamburger_restaurant", "american_restaurant"};
    private final static String[] includedTypesCoffee = {"coffee_shop", "cafe"};
    private final static String[] includedTypesIndianFood = {"indian_restaurant"};
    private final static String[] excludedTypesHamburger = {"pizza_restaurant", "middle_eastern_restaurant"};
    private final static String[] excludedTypesCoffee = {"tea_house"};
    private final static String[] includedTypesSeaFood = {"seafood_restaurant","sushi_restaurant"};
    private final static String[] includedTypesSteakHouse = {"steak_house"};
    private final static String[] includedTypesLebanese = {"lebanese_restaurant"};
    private final static String[] includedTypesItalian = {"italian_restaurant"};


    // utility class.
    private PlacesHelper() {
    }

    public static List<Place> getTop10Places(List<Place> places) {
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
    private static double calculateScore(Place place) {
        double rating = place.getRating();
        int count = place.getUserRatingCount();
        return (count > 0) ? rating * Math.log10(count) : 0;
    }

    public static List<Place> excludeChainRestaurants(List<Place> places) {
        List<String> chainRestaurants = List.of("McDonald", "KFC", "Burger King", "Kudu", "Shawarma House", "Hardee's", "Burgerizzr", "وهمي", "Hamburgini", "Sign", "Herfy", "wahmy");

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

    public static List<Place> excludeCoffees(List<Place> places) {
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


    public static String[] getIncludedTypes(String type) {
        return switch (type) {
            case "hamburger" -> includedTypesHamburger;
            case "coffee" -> includedTypesCoffee;
            case "indian" -> includedTypesIndianFood;
            case "seafood" -> includedTypesSeaFood;
            case "steakhouse" -> includedTypesSteakHouse;
            case "italian" -> includedTypesItalian;
            case "lebanese" -> includedTypesLebanese;
            default -> includedTypesDefault;
        };
    }

    public static String[] getExcludedTypes(String type) {
        if (type.contains("coffee")) {
            return excludedTypesCoffee;
        } else if (type.contains("hamburger")) {
            return excludedTypesHamburger;
        }
        // return excludedTypesHamburger for now.
        return excludedTypesHamburger;
    }
}
