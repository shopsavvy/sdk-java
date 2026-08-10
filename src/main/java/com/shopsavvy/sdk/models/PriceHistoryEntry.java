package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Historical price point.
 *
 * <p>The timestamp field is {@code timestamp}, matching the parent Offer's own
 * {@code timestamp} and the real wire shape ({@code {availability, price, timestamp}}).
 * Every SDK in the fleet mapped it from a {@code date} key — one the API has never sent —
 * until 2026-08-10, and Jackson's default ObjectMapper leaves an unmatched property null
 * rather than throwing, so {@code getDate()} returned null for every consumer
 * (ShopSavvy prospector-audit s28-t2-2).
 */
public class PriceHistoryEntry {
    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("availability")
    private String availability;

    public PriceHistoryEntry() {}

    public PriceHistoryEntry(String timestamp, Double price, String availability) {
        this.timestamp = timestamp;
        this.price = price;
        this.availability = availability;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
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
