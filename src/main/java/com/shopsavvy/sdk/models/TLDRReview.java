package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

public class TLDRReview {
    @JsonProperty("slug") private String slug;
    @JsonProperty("headline") private String headline;
    @JsonProperty("pros") private List<String> pros;
    @JsonProperty("cons") private List<String> cons;
    @JsonProperty("bottom_line") private String bottomLine;
    @JsonProperty("scores") private Map<String, Object> scores;

    public String getSlug() { return slug; }
    public String getHeadline() { return headline; }
    public List<String> getPros() { return pros; }
    public List<String> getCons() { return cons; }
    public String getBottomLine() { return bottomLine; }
    public Map<String, Object> getScores() { return scores; }
}
