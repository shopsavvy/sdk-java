package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Scheduled product model
 */
public class ScheduledProduct {
    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("identifier")
    private String identifier;

    @JsonProperty("frequency")
    private String frequency;

    @JsonProperty("retailer")
    private String retailer;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("last_refreshed")
    private String lastRefreshed;

    public ScheduledProduct() {}

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastRefreshed() {
        return lastRefreshed;
    }

    public void setLastRefreshed(String lastRefreshed) {
        this.lastRefreshed = lastRefreshed;
    }
}
