package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class Deal {
    @JsonProperty("path") private String path;
    @JsonProperty("title") private String title;
    @JsonProperty("subtitle") private String subtitle;
    @JsonProperty("description") private String description;
    @JsonProperty("emoji") private String emoji;
    @JsonProperty("grade") private Map<String, Object> grade;
    @JsonProperty("pricing") private Map<String, Object> pricing;
    @JsonProperty("retailer") private Map<String, String> retailer;
    @JsonProperty("product") private String product;
    @JsonProperty("url") private String url;
    @JsonProperty("image") private Map<String, Object> image;
    @JsonProperty("votes") private Map<String, Object> votes;
    @JsonProperty("comment_count") private int commentCount;
    @JsonProperty("tags") private List<Map<String, String>> tags;
    @JsonProperty("expires_at") private String expiresAt;
    @JsonProperty("created_at") private String createdAt;

    public String getPath() { return path; }
    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
    public String getDescription() { return description; }
    public String getEmoji() { return emoji; }
    public Map<String, Object> getGrade() { return grade; }
    public Map<String, Object> getPricing() { return pricing; }
    public Map<String, String> getRetailer() { return retailer; }
    public String getProduct() { return product; }
    public String getUrl() { return url; }
    public Map<String, Object> getImage() { return image; }
    public Map<String, Object> getVotes() { return votes; }
    public int getCommentCount() { return commentCount; }
    public List<Map<String, String>> getTags() { return tags; }
    public String getExpiresAt() { return expiresAt; }
    public String getCreatedAt() { return createdAt; }
}
