package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Product with nested offers (returned by offers endpoint)
 */
public class ProductWithOffers {
    @JsonProperty("title")
    private String title;

    @JsonProperty("shopsavvy")
    private String shopsavvy;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("category")
    private String category;

    @JsonProperty("images")
    private List<String> images;

    @JsonProperty("barcode")
    private String barcode;

    @JsonProperty("amazon")
    private String amazon;

    @JsonProperty("model")
    private String model;

    @JsonProperty("mpn")
    private String mpn;

    @JsonProperty("color")
    private String color;

    @JsonProperty("offers")
    private List<Offer> offers;

    public ProductWithOffers() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShopsavvy() {
        return shopsavvy;
    }

    public void setShopsavvy(String shopsavvy) {
        this.shopsavvy = shopsavvy;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getAmazon() {
        return amazon;
    }

    public void setAmazon(String amazon) {
        this.amazon = amazon;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMpn() {
        return mpn;
    }

    public void setMpn(String mpn) {
        this.mpn = mpn;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }
}
