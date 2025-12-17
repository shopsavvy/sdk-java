package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Product offer model
 */
public class Offer {
    @JsonProperty("id")
    private String id;

    @JsonProperty("retailer")
    private String retailer;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("availability")
    private String availability;

    @JsonProperty("condition")
    private String condition;

    @JsonProperty("URL")
    private String url;

    @JsonProperty("seller")
    private String seller;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("history")
    private List<PriceHistoryEntry> history;

    public Offer() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<PriceHistoryEntry> getHistory() {
        return history;
    }

    public void setHistory(List<PriceHistoryEntry> history) {
        this.history = history;
    }

    // Backward-compatible aliases

    /** @deprecated Use getId() instead */
    @Deprecated
    public String getOfferId() {
        return id;
    }

    /** @deprecated Use getUrl() instead */
    @Deprecated
    public String getOfferUrl() {
        return url;
    }

    /** @deprecated Use getTimestamp() instead */
    @Deprecated
    public String getLastUpdated() {
        return timestamp;
    }
}
