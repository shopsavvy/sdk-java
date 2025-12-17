package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * API response metadata containing credit usage info
 */
public class ApiMeta {
    @JsonProperty("credits_used")
    private Integer creditsUsed;

    @JsonProperty("credits_remaining")
    private Integer creditsRemaining;

    @JsonProperty("rate_limit_remaining")
    private Integer rateLimitRemaining;

    public ApiMeta() {}

    public ApiMeta(Integer creditsUsed, Integer creditsRemaining, Integer rateLimitRemaining) {
        this.creditsUsed = creditsUsed;
        this.creditsRemaining = creditsRemaining;
        this.rateLimitRemaining = rateLimitRemaining;
    }

    public Integer getCreditsUsed() {
        return creditsUsed;
    }

    public void setCreditsUsed(Integer creditsUsed) {
        this.creditsUsed = creditsUsed;
    }

    public Integer getCreditsRemaining() {
        return creditsRemaining;
    }

    public void setCreditsRemaining(Integer creditsRemaining) {
        this.creditsRemaining = creditsRemaining;
    }

    public Integer getRateLimitRemaining() {
        return rateLimitRemaining;
    }

    public void setRateLimitRemaining(Integer rateLimitRemaining) {
        this.rateLimitRemaining = rateLimitRemaining;
    }
}
