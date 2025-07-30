package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Offer with price history
 */
public class OfferWithHistory extends Offer {
    @JsonProperty("price_history")
    private List<PricePoint> priceHistory;
    
    public OfferWithHistory() {}
    
    public List<PricePoint> getPriceHistory() {
        return priceHistory;
    }
    
    public void setPriceHistory(List<PricePoint> priceHistory) {
        this.priceHistory = priceHistory;
    }
}