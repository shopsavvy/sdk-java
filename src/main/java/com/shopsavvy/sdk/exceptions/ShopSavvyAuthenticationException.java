package com.shopsavvy.sdk.exceptions;

/**
 * Exception thrown when API key authentication fails
 */
public class ShopSavvyAuthenticationException extends ShopSavvyApiException {
    public ShopSavvyAuthenticationException(String message) {
        super(message);
    }
    
    public ShopSavvyAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}