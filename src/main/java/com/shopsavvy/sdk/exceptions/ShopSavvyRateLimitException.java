package com.shopsavvy.sdk.exceptions;

/**
 * Exception thrown when API rate limits are exceeded
 */
public class ShopSavvyRateLimitException extends ShopSavvyApiException {
    public ShopSavvyRateLimitException(String message) {
        super(message);
    }
    
    public ShopSavvyRateLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}