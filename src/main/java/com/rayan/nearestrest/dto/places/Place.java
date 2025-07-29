package com.rayan.nearestrest.dto.places;

import com.rayan.nearestrest.core.enumeration.PriceLevel;

import java.util.List;

public class Place {

    private Double rating;
    private Integer userRatingCount;
    private PriceLevel priceLevel;
    private String formattedAddress;
    private List<String> types;
    private DisplayName displayName;
    private String googleMapsUri;

    public Place() {
    }

    public Place(Double rating, Integer userRatingCount, PriceLevel priceLevel, String formattedAddress, List<String> types, DisplayName displayName, String googleMapsUri) {
        this.rating = rating;
        this.userRatingCount = userRatingCount;
        this.priceLevel = priceLevel;
        this.formattedAddress = formattedAddress;
        this.types = types;
        this.displayName = displayName;
        this.googleMapsUri = googleMapsUri;
    }

    public String getGoogleMapsUri() {
        return googleMapsUri;
    }

    public void setGoogleMapsUri(String googleMapsUri) {
        this.googleMapsUri = googleMapsUri;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getUserRatingCount() {
        return userRatingCount;
    }

    public void setUserRatingCount(Integer userRatingCount) {
        this.userRatingCount = userRatingCount;
    }

    public PriceLevel getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(PriceLevel priceLevel) {
        this.priceLevel = priceLevel;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public DisplayName getDisplayName() {
        return displayName;
    }

    public void setDisplayName(DisplayName displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "Place{" +
                "rating=" + rating +
                ", userRatingCount=" + userRatingCount +
                ", priceLevel='" + priceLevel + '\'' +
                ", formattedAddress='" + formattedAddress + '\'' +
                ", types=" + types +
                ", displayName=" + displayName +
                '}';
    }
}
