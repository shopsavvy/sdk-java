package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Historical price point
 */
public class PriceHistoryEntry {
    @JsonProperty("date")
    private String date;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("availability")
    private String availability;

    public PriceHistoryEntry() {}

    public PriceHistoryEntry(String date, Double price, String availability) {
        this.date = date;
        this.price = price;
        this.availability = availability;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
