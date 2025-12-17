package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Product search result with pagination
 */
public class ProductSearchResult {
    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("data")
    private List<ProductDetails> data;

    @JsonProperty("pagination")
    private PaginationInfo pagination;

    @JsonProperty("meta")
    private ApiMeta meta;

    public ProductSearchResult() {}

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<ProductDetails> getData() {
        return data;
    }

    public void setData(List<ProductDetails> data) {
        this.data = data;
    }

    public PaginationInfo getPagination() {
        return pagination;
    }

    public void setPagination(PaginationInfo pagination) {
        this.pagination = pagination;
    }

    public ApiMeta getMeta() {
        return meta;
    }

    public void setMeta(ApiMeta meta) {
        this.meta = meta;
    }

    /**
     * Get credits used from meta object
     */
    public int creditsUsed() {
        return meta != null && meta.getCreditsUsed() != null ? meta.getCreditsUsed() : 0;
    }

    /**
     * Get credits remaining from meta object
     */
    public int creditsRemaining() {
        return meta != null && meta.getCreditsRemaining() != null ? meta.getCreditsRemaining() : 0;
    }
}
