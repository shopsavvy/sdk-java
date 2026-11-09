package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/** The store an {@link InStorePrice} was charged at. */
public class InStoreLocation {
    @JsonProperty("address")
    private String address;

    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    public InStoreLocation() {}

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}
