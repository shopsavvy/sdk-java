package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Current billing period details
 */
public class UsagePeriod {
    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("credits_used")
    private Integer creditsUsed;

    @JsonProperty("credits_limit")
    private Integer creditsLimit;

    @JsonProperty("credits_remaining")
    private Integer creditsRemaining;

    @JsonProperty("requests_made")
    private Integer requestsMade;

    public UsagePeriod() {}

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getCreditsUsed() {
        return creditsUsed;
    }

    public void setCreditsUsed(Integer creditsUsed) {
        this.creditsUsed = creditsUsed;
    }

    public Integer getCreditsLimit() {
        return creditsLimit;
    }

    public void setCreditsLimit(Integer creditsLimit) {
        this.creditsLimit = creditsLimit;
    }

    public Integer getCreditsRemaining() {
        return creditsRemaining;
    }

    public void setCreditsRemaining(Integer creditsRemaining) {
        this.creditsRemaining = creditsRemaining;
    }

    public Integer getRequestsMade() {
        return requestsMade;
    }

    public void setRequestsMade(Integer requestsMade) {
        this.requestsMade = requestsMade;
    }
}
