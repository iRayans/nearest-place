package com.rayan.nearestrest.dto;


import com.rayan.nearestrest.core.enumeration.PriceLevel;

public class RestaurantResultDTO {
    private String restaurantName;
    private double rating;
    private int ratingCount;
    private String googleMapsUri;
    private PriceLevel priceLevel;

    public RestaurantResultDTO() {
    }

    public RestaurantResultDTO(String restaurantName, double rating, int ratingCount, String googleMapsUri, PriceLevel priceLevel) {
        this.restaurantName = restaurantName;
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.googleMapsUri = googleMapsUri;
        this.priceLevel = priceLevel;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public String getGoogleMapsUri() {
        return googleMapsUri;
    }

    public void setGoogleMapsUri(String googleMapsUri) {
        this.googleMapsUri = googleMapsUri;
    }

    public PriceLevel getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(PriceLevel priceLevel) {
        this.priceLevel = priceLevel;
    }

    @Override
    public String toString() {
        return "BestResultsResponse{" +
                "restaurantName='" + restaurantName + '\'' +
                ", rating=" + rating +
                ", ratingCount=" + ratingCount +
                ", googleMapsUri='" + googleMapsUri + '\'' +
                ", priceLevel=" + priceLevel +
                '}';
    }
}
