package com.rayan.nearestrest.dto.places;

public class DisplayName {
    private String text;

    public DisplayName() {
    }

    public DisplayName(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "DisplayName{" +
                "text='" + text + '\'' +
                '}';
    }
}
