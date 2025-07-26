package com.rayan.nearestrest.dto.textSearch;

public class PlacesTextSearchRequest {
    private String textQuery;
    private LocationBias locationBias;
    private Integer maxResultCount;
    private String rankPreference;
    private double minRating;



    public PlacesTextSearchRequest() {
    }

    public PlacesTextSearchRequest(String textQuery, LocationBias locationBias, Integer maxResultCount, String rankPreference, double minRating) {
        this.textQuery = textQuery;
        this.locationBias = locationBias;
        this.maxResultCount = maxResultCount;
        this.rankPreference = rankPreference;
        this.minRating = minRating;
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

    public double getMinRating() {
        return minRating;
    }

    public void setMinRating(double minRating) {
        this.minRating = minRating;
    }
}
