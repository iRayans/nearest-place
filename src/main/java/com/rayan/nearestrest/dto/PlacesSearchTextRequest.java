package com.rayan.nearestrest.dto;

public class PlacesSearchTextRequest {
    private String textQuery;
    private LocationBias locationBias;
    private Integer maxResultCount;
    private String rankPreference;
    private Integer minRating;


    public PlacesSearchTextRequest() {
    }

    public PlacesSearchTextRequest(String textQuery, LocationBias locationBias, Integer maxResultCount, String rankPreference, Integer minRating) {
        this.textQuery = textQuery;
        this.locationBias = locationBias;
        this.maxResultCount = maxResultCount;
        this.rankPreference = rankPreference;
        this.minRating = minRating;
    }

    public static class LocationBias {
        public Circle circle;
    }

    public static class Circle {
        public Center center;
        public Double radius;
    }

    public static class Center {
        public Double latitude;
        public Double longitude;
    }

    public String getTextQuery() {
        return textQuery;
    }

    public void setTextQuery(String textQuery) {
        this.textQuery = textQuery;
    }

    public LocationBias getLocationBias() {
        return locationBias;
    }

    public void setLocationBias(LocationBias locationBias) {
        this.locationBias = locationBias;
    }

    public Integer getMaxResultCount() {
        return maxResultCount;
    }

    public void setMaxResultCount(Integer maxResultCount) {
        this.maxResultCount = maxResultCount;
    }

    public String getRankPreference() {
        return rankPreference;
    }

    public void setRankPreference(String rankPreference) {
        this.rankPreference = rankPreference;
    }

    public Integer getMinRating() {
        return minRating;
    }

    public void setMinRating(Integer minRating) {
        this.minRating = minRating;
    }
}
