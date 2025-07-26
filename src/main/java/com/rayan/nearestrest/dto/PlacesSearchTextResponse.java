package com.rayan.nearestrest.dto;

import java.util.List;

public class PlacesSearchTextResponse {
    public List<Place> places;

    public static class Place {
        public DisplayName displayName;
        public Double rating;
        public Integer userRatingCount;
        public String priceLevel;
        public String formattedAddress;
        public List<String> types;

        public static class DisplayName {
            public String text;
        }
    }
}