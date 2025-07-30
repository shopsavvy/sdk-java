package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Request model for removing scheduled products
 */
public class RemoveRequest {
    @JsonProperty("identifier")
    private String identifier;
    
    public RemoveRequest() {}
    
    public RemoveRequest(String identifier) {
        this.identifier = identifier;
    }
    
    public String getIdentifier() {
        return identifier;
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}