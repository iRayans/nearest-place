package com.rayan.nearestrest.dto.nearbySearch;

import com.rayan.nearestrest.dto.textSearch.Circle;

public class LocationRestriction {
    private Circle circle;

    public LocationRestriction() {
    }

    public LocationRestriction(Circle circle) {
        this.circle = circle;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    @Override
    public String toString() {
        return "LocationRestriction{" +
                "circle=" + circle +
                '}';
    }
}
