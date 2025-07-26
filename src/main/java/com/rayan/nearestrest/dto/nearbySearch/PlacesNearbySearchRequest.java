package com.rayan.nearestrest.dto.nearbySearch;


import java.util.Arrays;

public class PlacesNearbySearchRequest {
    private String[] includedTypes;
    private Integer maxResultCount;
    private String rankPreference;
    private LocationRestriction locationRestriction;



    public PlacesNearbySearchRequest() {
    }

    public PlacesNearbySearchRequest(String[] includedTypes, Integer maxResultCount, String rankPreference, LocationRestriction locationRestriction) {
        this.includedTypes = includedTypes;
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

    @Override
    public String toString() {
        return "PlacesnearbySearchRequest{" +
                "includedTypes=" + Arrays.toString(includedTypes) +
                ", maxResultCount=" + maxResultCount +
                ", rankPreference='" + rankPreference + '\'' +
                ", locationRestriction=" + locationRestriction +
                '}';
    }
}
