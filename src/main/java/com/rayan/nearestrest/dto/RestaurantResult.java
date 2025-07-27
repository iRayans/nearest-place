package com.rayan.nearestrest.dto;

import java.util.List;

public class RestaurantResult {

        private List<RestaurantResultDTO> results;

    public RestaurantResult() {
    }

    public RestaurantResult(List<RestaurantResultDTO> results) {
        this.results = results;
    }

    public List<RestaurantResultDTO> getResults() {
            return results;
        }

        public void setResults(List<RestaurantResultDTO> results) {
            this.results = results;
        }
}
