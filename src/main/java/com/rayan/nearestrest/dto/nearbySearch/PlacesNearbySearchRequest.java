package com.rayan.nearestrest.dto.nearbySearch;


import java.util.Arrays;

public class PlacesNearbySearchRequest {
    private String[] includedTypes;
    private String[] excludedTypes;
    private Integer maxResultCount;
    private String rankPreference;
    private LocationRestriction locationRestriction;



    public PlacesNearbySearchRequest() {
    }

    public PlacesNearbySearchRequest(String[] includedTypes, String[] excludedTypes, Integer maxResultCount, String rankPreference, LocationRestriction locationRestriction) {
        this.includedTypes = includedTypes;
        this.excludedTypes = excludedTypes;
        this.maxResultCount = maxResultCount;
        this.rankPreference = rankPreference;
        this.locationRestriction = locationRestriction;
    }

    public String[] getIncludedTypes() {
        return includedTypes;
    }

    public void setIncludedTypes(String[] includedTypes) {
        this.includedTypes = includedTypes;
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

    public LocationRestriction getLocationRestriction() {
        return locationRestriction;
    }

    public void setLocationRestriction(LocationRestriction locationRestriction) {
        this.locationRestriction = locationRestriction;
    }

    public String[] getExcludedTypes() {
        return excludedTypes;
    }

    public void setExcludedTypes(String[] excludedTypes) {
        this.excludedTypes = excludedTypes;
    }

    @Override
    public String toString() {
        return "PlacesNearbySearchRequest{" +
                "includedTypes=" + Arrays.toString(includedTypes) +
                ", excludedTypes=" + Arrays.toString(excludedTypes) +
                ", maxResultCount=" + maxResultCount +
                ", rankPreference='" + rankPreference + '\'' +
                ", locationRestriction=" + locationRestriction +
                '}';
    }
}
