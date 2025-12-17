package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response model for scheduling operations
 */
public class ScheduleResponse {
    @JsonProperty("scheduled")
    private Boolean scheduled;

    @JsonProperty("product_id")
    private String productId;

    public ScheduleResponse() {}

    public ScheduleResponse(Boolean scheduled, String productId) {
        this.scheduled = scheduled;
        this.productId = productId;
    }

    public Boolean getScheduled() {
        return scheduled;
    }

    public void setScheduled(Boolean scheduled) {
        this.scheduled = scheduled;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
