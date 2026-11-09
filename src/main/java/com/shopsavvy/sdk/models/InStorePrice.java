package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A shelf price a physical store charged on a given day, read from a shopper's scanned
 * receipt. {@code source} is always {@code "user-collected"}. It says what the store sold the
 * product for that day, not that it is in stock now; nothing about the shopper is included.
 */
public class InStorePrice {
    @JsonProperty("source")
    private String source;

    @JsonProperty("retailer")
    private String retailer;

    @JsonProperty("store")
    private InStoreLocation store;

    /** Unit shelf price paid (the median when several receipts from the store agree). */
    @JsonProperty("price")
    private Double price;

    @JsonProperty("currency")
    private String currency;

    /** YYYY-MM-DD date of the most recent receipt behind the price. */
    @JsonProperty("observed_on")
    private String observedOn;

    @JsonProperty("timestamp")
    private String timestamp;

    public InStorePrice() {}

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getRetailer() { return retailer; }
    public void setRetailer(String retailer) { this.retailer = retailer; }
    public InStoreLocation getStore() { return store; }
    public void setStore(InStoreLocation store) { this.store = store; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getObservedOn() { return observedOn; }
    public void setObservedOn(String observedOn) { this.observedOn = observedOn; }
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
