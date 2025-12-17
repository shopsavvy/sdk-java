package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model for removal operations
 */
public class RemoveResponse {
    @JsonProperty("removed")
    private Boolean removed;

    public RemoveResponse() {}

    public RemoveResponse(Boolean removed) {
        this.removed = removed;
    }

    public Boolean getRemoved() {
        return removed;
    }

    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }
}
