package com.rayan.nearestrest.dto.textSearch;

import com.rayan.nearestrest.dto.Circle;

public class LocationBias {
    private Circle circle;

    public LocationBias() {
    }

    public LocationBias(Circle circle) {
        this.circle = circle;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }
}
