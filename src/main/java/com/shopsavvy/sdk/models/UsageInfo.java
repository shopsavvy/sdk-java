package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * API usage information model
 */
public class UsageInfo {
    @JsonProperty("current_period")
    private UsagePeriod currentPeriod;

    @JsonProperty("usage_percentage")
    private Double usagePercentage;

    public UsageInfo() {}

    public UsagePeriod getCurrentPeriod() {
        return currentPeriod;
    }

    public void setCurrentPeriod(UsagePeriod currentPeriod) {
        this.currentPeriod = currentPeriod;
    }

    public Double getUsagePercentage() {
        return usagePercentage;
    }

    public void setUsagePercentage(Double usagePercentage) {
        this.usagePercentage = usagePercentage;
    }

    // Backward-compatible methods

    /** @deprecated Use getCurrentPeriod().getCreditsUsed() instead */
    @Deprecated
    public Integer getCreditsUsed() {
        return currentPeriod != null ? currentPeriod.getCreditsUsed() : null;
    }

    /** @deprecated Use getCurrentPeriod().getCreditsRemaining() instead */
    @Deprecated
    public Integer getCreditsRemaining() {
        return currentPeriod != null ? currentPeriod.getCreditsRemaining() : null;
    }

    /** @deprecated Use getCurrentPeriod().getCreditsLimit() instead */
    @Deprecated
    public Integer getCreditsTotal() {
        return currentPeriod != null ? currentPeriod.getCreditsLimit() : null;
    }

    /** @deprecated Use getCurrentPeriod().getStartDate() instead */
    @Deprecated
    public String getBillingPeriodStart() {
        return currentPeriod != null ? currentPeriod.getStartDate() : null;
    }

    /** @deprecated Use getCurrentPeriod().getEndDate() instead */
    @Deprecated
    public String getBillingPeriodEnd() {
        return currentPeriod != null ? currentPeriod.getEndDate() : null;
    }
}
