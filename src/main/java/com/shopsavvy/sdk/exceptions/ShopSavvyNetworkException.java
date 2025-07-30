package com.shopsavvy.sdk.exceptions;

/**
 * Exception thrown when network errors occur
 */
public class ShopSavvyNetworkException extends ShopSavvyApiException {
    public ShopSavvyNetworkException(String message) {
        super(message);
    }
    
    public ShopSavvyNetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}