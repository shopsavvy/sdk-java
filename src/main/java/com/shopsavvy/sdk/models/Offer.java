package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Product offer model
 */
public class Offer {
    @JsonProperty("retailer")
    private String retailer;
    
    @JsonProperty("price")
    private Double price;
    
    @JsonProperty("currency")
    private String currency;
    
    @JsonProperty("availability")
    private String availability;
    
    @JsonProperty("condition")
    private String condition;
    
    @JsonProperty("shipping_cost")
    private Double shippingCost;
    
    @JsonProperty("url")
    private String url;
    
    @JsonProperty("last_updated")
    private String lastUpdated;
    
    public Offer() {}
    
    public String getRetailer() {
        return retailer;
    }
    
    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getAvailability() {
        return availability;
    }
    
    public void setAvailability(String availability) {
        this.availability = availability;
    }
    
    public String getCondition() {
        return condition;
    }
    
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    public Double getShippingCost() {
        return shippingCost;
    }
    
    public void setShippingCost(Double shippingCost) {
        this.shippingCost = shippingCost;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}