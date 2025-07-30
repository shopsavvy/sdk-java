package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Scheduled product model
 */
public class ScheduledProduct {
    @JsonProperty("identifier")
    private String identifier;
    
    @JsonProperty("frequency")
    private String frequency;
    
    @JsonProperty("retailer")
    private String retailer;
    
    @JsonProperty("created_at")
    private String createdAt;
    
    @JsonProperty("last_updated")
    private String lastUpdated;
    
    public ScheduledProduct() {}
    
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
    
    public String getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}