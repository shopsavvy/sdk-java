package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Product details model
 */
public class ProductDetails {
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

    @JsonProperty("title_short")
    private String titleShort;

    @JsonProperty("slug")
    private String slug;

    @JsonProperty("description")
    private String description;

    @JsonProperty("categories")
    private List<String> categories;

    @JsonProperty("attributes")
    private java.util.Map<String, String> attributes;

    @JsonProperty("rating")
    private java.util.Map<String, Object> rating;

    /**
     * Expert quality scores on a 0-1 scale (multiply by 10 or 100 for display):
     * "overall", "customer", "professional", plus an "aspects" map keyed by
     * free-form aspect names from the product's professional reviews.
     */
    @JsonProperty("score")
    private java.util.Map<String, Object> score;

    @JsonProperty("keywords")
    private List<String> keywords;

    @JsonProperty("identifiers")
    private java.util.Map<String, Object> identifiers;

    public ProductDetails() {}

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

    // Backward-compatible aliases

    /** @deprecated Use getTitle() instead */
    @Deprecated
    public String getName() {
        return title;
    }

    /** @deprecated Use getShopsavvy() instead */
    @Deprecated
    public String getProductId() {
        return shopsavvy;
    }

    /** @deprecated Use getAmazon() instead */
    @Deprecated
    public String getAsin() {
        return amazon;
    }

    /** @deprecated Use getImages() and get first item instead */
    @Deprecated
    public String getImageUrl() {
        return images != null && !images.isEmpty() ? images.get(0) : null;
    }

    public String getTitleShort() { return titleShort; }
    public String getSlug() { return slug; }
    public String getDescription() { return description; }
    public List<String> getCategories() { return categories; }
    public java.util.Map<String, String> getAttributes() { return attributes; }
    public java.util.Map<String, Object> getRating() { return rating; }
    public java.util.Map<String, Object> getScore() { return score; }
    public List<String> getKeywords() { return keywords; }
    public java.util.Map<String, Object> getIdentifiers() { return identifiers; }
}
