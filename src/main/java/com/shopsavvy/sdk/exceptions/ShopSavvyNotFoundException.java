package com.shopsavvy.sdk.exceptions;

/**
 * Exception thrown when a requested resource is not found
 */
public class ShopSavvyNotFoundException extends ShopSavvyApiException {
    public ShopSavvyNotFoundException(String message) {
        super(message);
    }
    
    public ShopSavvyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}