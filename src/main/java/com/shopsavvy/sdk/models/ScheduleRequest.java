package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request model for scheduling product monitoring
 */
public class ScheduleRequest {
    @JsonProperty("identifier")
    private String identifier;
    
    @JsonProperty("frequency")
    private String frequency;
    
    @JsonProperty("retailer")
    private String retailer;
    
    public ScheduleRequest() {}
    
    public ScheduleRequest(String identifier, String frequency, String retailer) {
        this.identifier = identifier;
        this.frequency = frequency;
        this.retailer = retailer;
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
}