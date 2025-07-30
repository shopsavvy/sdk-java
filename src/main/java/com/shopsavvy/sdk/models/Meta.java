package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * API response metadata
 */
public class Meta {
    @JsonProperty("requestId")
    private String requestId;
    
    @JsonProperty("timestamp")
    private String timestamp;
    
    @JsonProperty("cached")
    private Boolean cached;
    
    @JsonProperty("credits_used")
    private Integer creditsUsed;
    
    public Meta() {}
    
    public String getRequestId() {
        return requestId;
    }
    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    public Boolean getCached() {
        return cached;
    }
    
    public void setCached(Boolean cached) {
        this.cached = cached;
    }
    
    public Integer getCreditsUsed() {
        return creditsUsed;
    }
    
    public void setCreditsUsed(Integer creditsUsed) {
        this.creditsUsed = creditsUsed;
    }
}