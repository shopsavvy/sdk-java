package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Generic API response wrapper
 */
public class ApiResponse<T> {
    @JsonProperty("data")
    private T data;
    
    @JsonProperty("meta")
    private Meta meta;
    
    public ApiResponse() {}
    
    public ApiResponse(T data, Meta meta) {
        this.data = data;
        this.meta = meta;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public Meta getMeta() {
        return meta;
    }
    
    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}