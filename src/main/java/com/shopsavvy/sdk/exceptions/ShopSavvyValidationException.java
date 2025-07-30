package com.shopsavvy.sdk.exceptions;

/**
 * Exception thrown when request parameters fail validation
 */
public class ShopSavvyValidationException extends ShopSavvyApiException {
    public ShopSavvyValidationException(String message) {
        super(message);
    }
    
    public ShopSavvyValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}