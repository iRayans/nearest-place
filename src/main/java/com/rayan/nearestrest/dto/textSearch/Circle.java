package com.rayan.nearestrest.dto.textSearch;

public class Circle {
    private Center center;
    private Double radius;

    public Circle() {
    }

    public Circle(Center center, Double radius) {
        this.center = center;
        this.radius = radius;
    }

    public Center getCenter() {
        return center;
    }

    public void setCenter(Center center) {
        this.center = center;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }
}
