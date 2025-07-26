package com.rayan.nearestrest.dto;

public class PlacesSearchTextRequest {
    public String textQuery;
    public LocationBias locationBias;
    public Integer maxResultCount;
    public String rankPreference;
    public Integer minRating;

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
}
