package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Offer returned by {@code getPriceHistory()}, i.e. one carrying its {@code history} array.
 *
 * <p>This used to bind the list to a {@code price_history} JSON property. The API has never
 * sent a key by that name — history has always arrived under {@code history} — so
 * {@code getPriceHistory()} returned null for every caller, on every successful response
 * (ShopSavvy prospector-audit s28-t2-2). The accessor pair is renamed with the property so a
 * consumer cannot keep reading the dead one by accident.
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

    @JsonProperty("history")
    private List<PriceHistoryEntry> history;

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

    public List<PriceHistoryEntry> getHistory() {
        return history;
    }

    public void setHistory(List<PriceHistoryEntry> history) {
        this.history = history;
    }
}
