package com.rayan.nearestrest.service;

import com.rayan.nearestrest.clinet.GooglePlacesClientNearbySearch;
import com.rayan.nearestrest.core.exception.EntityInvalidArgumentsException;
import com.rayan.nearestrest.core.exception.ResultNotFoundException;
import com.rayan.nearestrest.core.helper.PlacesHelper;
import com.rayan.nearestrest.dto.*;
import com.rayan.nearestrest.dto.nearbySearch.LocationRestriction;
import com.rayan.nearestrest.dto.request.PlacesNearbySearchRequest;
import com.rayan.nearestrest.dto.places.Place;
import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.stream.Collectors;

import static com.rayan.nearestrest.core.enumeration.PriceLevel.PRICE_LEVEL_UNKNOWN;
import static com.rayan.nearestrest.core.helper.PlacesHelper.*;

@ApplicationScoped
public class NearbySearchService {

    private static final Logger LOGGER = Logger.getLogger(NearbySearchService.class);

    @ConfigProperty(name = "api.key")
    private String apiKey;

    @Inject
    @RestClient
    GooglePlacesClientNearbySearch nearbyPlacesClient;

    @CacheResult(cacheName = "places")
    public RestaurantResult searchRestaurants(double lat, double lng, String type) {
        try {
            LOGGER.infof("Constructing request for %s at %.4f, %.4f", type, lat, lng);
            PlacesNearbySearchRequest req = constructRequest(lat, lng, type);
            String fieldMask = "places.displayName,places.rating,places.userRatingCount,places.priceLevel,places.formattedAddress,places.types,places.googleMapsUri,places.priceLevel";
            PlacesTextSearchResponse res = nearbyPlacesClient.searchPlaces(req, apiKey, fieldMask);

            List<Place> places = res.getPlaces() == null ? List.of() : res.getPlaces();
            if (type.contains("hamburger") || type.contains("restaurant")) {
                places = excludeChainRestaurants(res.getPlaces());
            } else if (type.contains("coffee")) {
                places = excludeCoffees(res.getPlaces());
            }

            List<Place> top10Places = PlacesHelper.getTop10Places(places);
            List<RestaurantResultDTO> resultDTOs = parseRestaurants(top10Places);
            return new RestaurantResult(resultDTOs);

        } catch (WebApplicationException e) {
            if (e.getResponse().getStatus() == 400) {
                LOGGER.error("Bad request to Google Places API. Check your request payload or headers.");
                throw new EntityInvalidArgumentsException("Invalid location coordinates.Please try again.");
            } else {
                LOGGER.error("Unexpected response from Google Places API: " + e.getMessage(), e);
                throw new ResultNotFoundException("External service failed. Please try again later.");
            }
        } catch (Exception e) {
            LOGGER.error("Failed to search restaurants: " + e.getMessage(), e);
            throw new ResultNotFoundException("Failed to retrieve restaurants. Please try again later.");
        }

    }

    // Helpers methods

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
        PlacesNearbySearchRequest req = new PlacesNearbySearchRequest();
        req.setRankPreference("POPULARITY");
        req.setMaxResultCount(20);

        if (type == null) {
            // if 'type' for some reason is null, set it to 'Hamburger'.
            type = "restaurant";
        }
        req.setIncludedTypes(getIncludedTypes(type));
        if (!type.equals("restaurant")) {
            req.setExcludedTypes(PlacesHelper.getExcludedTypes(type));
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
}