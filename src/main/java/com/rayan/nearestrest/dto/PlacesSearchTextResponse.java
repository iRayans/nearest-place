package com.rayan.nearestrest.dto;

import com.rayan.nearestrest.dto.places.Place;

import java.util.List;

public class PlacesSearchTextResponse {
    private List<Place> places;


    public PlacesSearchTextResponse() {
    }

    public PlacesSearchTextResponse(List<Place> places) {
        this.places = places;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    @Override
    public String toString() {
        return "PlacesSearchTextResponse{" +
                "places=" + places +
                '}';
    }
}