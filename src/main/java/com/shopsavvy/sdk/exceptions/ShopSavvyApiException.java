package com.shopsavvy.sdk.exceptions;

/**
 * Base exception for ShopSavvy API errors
 */
public class ShopSavvyApiException extends Exception {
    public ShopSavvyApiException(String message) {
        super(message);
    }
    
    public ShopSavvyApiException(String message, Throwable cause) {
        super(message, cause);
    }
}