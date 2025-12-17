package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Offer with price history
 */
public class OfferWithHistory {
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

    @JsonProperty("price_history")
    private List<PriceHistoryEntry> priceHistory;

    public OfferWithHistory() {}

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

    public List<PriceHistoryEntry> getPriceHistory() {
        return priceHistory;
    }

    public void setPriceHistory(List<PriceHistoryEntry> priceHistory) {
        this.priceHistory = priceHistory;
    }
}
