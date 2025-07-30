package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * API usage information model
 */
public class UsageInfo {
    @JsonProperty("credits_used")
    private Integer creditsUsed;
    
    @JsonProperty("credits_remaining")
    private Integer creditsRemaining;
    
    @JsonProperty("credits_limit")
    private Integer creditsLimit;
    
    @JsonProperty("reset_date")
    private String resetDate;
    
    @JsonProperty("current_period_start")
    private String currentPeriodStart;
    
    @JsonProperty("current_period_end")
    private String currentPeriodEnd;
    
    public UsageInfo() {}
    
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
    
    public Integer getCreditsLimit() {
        return creditsLimit;
    }
    
    public void setCreditsLimit(Integer creditsLimit) {
        this.creditsLimit = creditsLimit;
    }
    
    public String getResetDate() {
        return resetDate;
    }
    
    public void setResetDate(String resetDate) {
        this.resetDate = resetDate;
    }
    
    public String getCurrentPeriodStart() {
        return currentPeriodStart;
    }
    
    public void setCurrentPeriodStart(String currentPeriodStart) {
        this.currentPeriodStart = currentPeriodStart;
    }
    
    public String getCurrentPeriodEnd() {
        return currentPeriodEnd;
    }
    
    public void setCurrentPeriodEnd(String currentPeriodEnd) {
        this.currentPeriodEnd = currentPeriodEnd;
    }
}