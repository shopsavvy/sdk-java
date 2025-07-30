package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Historical price point
 */
public class PricePoint {
    @JsonProperty("date")
    private String date;
    
    @JsonProperty("price")
    private Double price;
    
    @JsonProperty("availability")
    private String availability;
    
    public PricePoint() {}
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public String getAvailability() {
        return availability;
    }
    
    public void setAvailability(String availability) {
        this.availability = availability;
    }
}