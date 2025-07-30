package com.shopsavvy.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.shopsavvy.sdk.exceptions.*;
import com.shopsavvy.sdk.models.*;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Official Java client for ShopSavvy Data API
 * 
 * <p>Provides access to product data, pricing information, and price history
 * across thousands of retailers and millions of products.</p>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * ShopSavvyClient client = new ShopSavvyClient("ss_live_your_api_key_here");
 * 
 * ApiResponse<ProductDetails> product = client.getProductDetails("012345678901");
 * System.out.println("Product: " + product.getData().getName());
 * 
 * client.close();
 * }</pre>
 */
public class ShopSavvyClient implements AutoCloseable {
    
    private static final String DEFAULT_BASE_URL = "https://api.shopsavvy.com/v1";
    private static final Pattern API_KEY_PATTERN = Pattern.compile("^ss_(live|test)_[a-zA-Z0-9]+$");
    
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    
    /**
     * Create a new ShopSavvy Data API client
     * 
     * @param apiKey Your ShopSavvy API key
     */
    public ShopSavvyClient(@NotNull String apiKey) {
        this(apiKey, DEFAULT_BASE_URL, 30);
    }
    
    /**
     * Create a new ShopSavvy Data API client with custom configuration
     * 
     * @param apiKey Your ShopSavvy API key
     * @param baseUrl Base URL for the API
     * @param timeoutSeconds Request timeout in seconds
     */
    public ShopSavvyClient(@NotNull String apiKey, @NotNull String baseUrl, int timeoutSeconds) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("API key is required. Get one at https://shopsavvy.com/data");
        }
        
        if (!API_KEY_PATTERN.matcher(apiKey).matches()) {
            throw new IllegalArgumentException("Invalid API key format. API keys should start with ss_live_ or ss_test_");
        }
        
        this.baseUrl = baseUrl;
        this.objectMapper = new ObjectMapper();
        
        this.httpClient = new OkHttpClient.Builder()
            .connectTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .writeTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .addInterceptor(chain -> {
                Request originalRequest = chain.request();
                Request newRequest = originalRequest.newBuilder()
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .header("User-Agent", "ShopSavvy-Java-SDK/1.0.0")
                    .build();
                return chain.proceed(newRequest);
            })
            .build();
    }
    
    /**
     * Look up product details by identifier
     * 
     * @param identifier Product identifier (barcode, ASIN, URL, model number, or ShopSavvy product ID)
     * @return Product details
     * @throws ShopSavvyApiException if the API request fails
     */
    @NotNull
    public ApiResponse<ProductDetails> getProductDetails(@NotNull String identifier) throws ShopSavvyApiException {
        return getProductDetails(identifier, null);
    }
    
    /**
     * Look up product details by identifier
     * 
     * @param identifier Product identifier
     * @param format Response format ('json' or 'csv')
     * @return Product details
     * @throws ShopSavvyApiException if the API request fails
     */
    @NotNull
    public ApiResponse<ProductDetails> getProductDetails(@NotNull String identifier, @Nullable String format) throws ShopSavvyApiException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + "/products/details").newBuilder()
            .addQueryParameter("identifier", identifier);
        
        if (format != null) {
            urlBuilder.addQueryParameter("format", format);
        }
        
        Request request = new Request.Builder()
            .url(urlBuilder.build())
            .get()
            .build();
        
        return executeRequest(request, ProductDetails.class);
    }
    
    /**
     * Look up details for multiple products
     * 
     * @param identifiers List of product identifiers
     * @return List of product details
     * @throws ShopSavvyApiException if the API request fails
     */
    @NotNull
    public ApiResponse<List<ProductDetails>> getProductDetailsBatch(@NotNull List<String> identifiers) throws ShopSavvyApiException {
        return getProductDetailsBatch(identifiers, null);
    }
    
    /**
     * Look up details for multiple products
     * 
     * @param identifiers List of product identifiers
     * @param format Response format ('json' or 'csv')
     * @return List of product details
     * @throws ShopSavvyApiException if the API request fails
     */
    @NotNull
    public ApiResponse<List<ProductDetails>> getProductDetailsBatch(@NotNull List<String> identifiers, @Nullable String format) throws ShopSavvyApiException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + "/products/details").newBuilder()
            .addQueryParameter("identifiers", String.join(",", identifiers));
        
        if (format != null) {
            urlBuilder.addQueryParameter("format", format);
        }
        
        Request request = new Request.Builder()
            .url(urlBuilder.build())
            .get()
            .build();
        
        return executeRequestForList(request, ProductDetails.class);
    }
    
    /**
     * Get current offers for a product
     * 
     * @param identifier Product identifier
     * @return Current offers
     * @throws ShopSavvyApiException if the API request fails
     */
    @NotNull
    public ApiResponse<List<Offer>> getCurrentOffers(@NotNull String identifier) throws ShopSavvyApiException {
        return getCurrentOffers(identifier, null, null);
    }
    
    /**
     * Get current offers for a product
     * 
     * @param identifier Product identifier
     * @param retailer Optional retailer to filter by
     * @param format Response format ('json' or 'csv')
     * @return Current offers
     * @throws ShopSavvyApiException if the API request fails
     */
    @NotNull
    public ApiResponse<List<Offer>> getCurrentOffers(@NotNull String identifier, @Nullable String retailer, @Nullable String format) throws ShopSavvyApiException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + "/products/offers").newBuilder()
            .addQueryParameter("identifier", identifier);
        
        if (retailer != null) {
            urlBuilder.addQueryParameter("retailer", retailer);
        }
        if (format != null) {
            urlBuilder.addQueryParameter("format", format);
        }
        
        Request request = new Request.Builder()
            .url(urlBuilder.build())
            .get()
            .build();
        
        return executeRequestForList(request, Offer.class);
    }
    
    /**
     * Get current offers for multiple products
     * 
     * @param identifiers List of product identifiers
     * @return Map of identifiers to their offers
     * @throws ShopSavvyApiException if the API request fails
     */
    @NotNull
    public ApiResponse<Map<String, List<Offer>>> getCurrentOffersBatch(@NotNull List<String> identifiers) throws ShopSavvyApiException {
        return getCurrentOffersBatch(identifiers, null, null);
    }
    
    /**
     * Get current offers for multiple products
     * 
     * @param identifiers List of product identifiers
     * @param retailer Optional retailer to filter by
     * @param format Response format ('json' or 'csv')
     * @return Map of identifiers to their offers
     * @throws ShopSavvyApiException if the API request fails
     */
    @NotNull
    public ApiResponse<Map<String, List<Offer>>> getCurrentOffersBatch(@NotNull List<String> identifiers, @Nullable String retailer, @Nullable String format) throws ShopSavvyApiException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + "/products/offers").newBuilder()
            .addQueryParameter("identifiers", String.join(",", identifiers));
        
        if (retailer != null) {
            urlBuilder.addQueryParameter("retailer", retailer);
        }
        if (format != null) {
            urlBuilder.addQueryParameter("format", format);
        }
        
        Request request = new Request.Builder()
            .url(urlBuilder.build())
            .get()
            .build();
        
        return executeRequestForMap(request, Offer.class);
    }
    
    /**
     * Get price history for a product
     * 
     * @param identifier Product identifier
     * @param startDate Start date (YYYY-MM-DD format)
     * @param endDate End date (YYYY-MM-DD format)
     * @return Offers with price history
     * @throws ShopSavvyApiException if the API request fails
     */
    @NotNull
    public ApiResponse<List<OfferWithHistory>> getPriceHistory(@NotNull String identifier, @NotNull String startDate, @NotNull String endDate) throws ShopSavvyApiException {
        return getPriceHistory(identifier, startDate, endDate, null, null);
    }
    
    /**
     * Get price history for a product
     * 
     * @param identifier Product identifier
     * @param startDate Start date (YYYY-MM-DD format)
     * @param endDate End date (YYYY-MM-DD format)
     * @param retailer Optional retailer to filter by
     * @param format Response format ('json' or 'csv')
     * @return Offers with price history
     * @throws ShopSavvyApiException if the API request fails
     */
    @NotNull
    public ApiResponse<List<OfferWithHistory>> getPriceHistory(@NotNull String identifier, @NotNull String startDate, @NotNull String endDate, @Nullable String retailer, @Nullable String format) throws ShopSavvyApiException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + "/products/history").newBuilder()
            .addQueryParameter("identifier", identifier)
            .addQueryParameter("start_date", startDate)
            .addQueryParameter("end_date", endDate);
        
        if (retailer != null) {
            urlBuilder.addQueryParameter("retailer", retailer);
        }
        if (format != null) {
            urlBuilder.addQueryParameter("format", format);
        }
        
        Request request = new Request.Builder()
            .url(urlBuilder.build())
            .get()
            .build();
        
        return executeRequestForList(request, OfferWithHistory.class);
    }
    
    /**
     * Schedule product monitoring
     * 
     * @param identifier Product identifier
     * @param frequency How often to refresh ('hourly', 'daily', 'weekly')
     * @return Scheduling confirmation
     * @throws ShopSavvyApiException if the API request fails
     */
    @NotNull
    public ApiResponse<ScheduleResponse> scheduleProductMonitoring(@NotNull String identifier, @NotNull String frequency) throws ShopSavvyApiException {
        return scheduleProductMonitoring(identifier, frequency, null);
    }
    
    /**
     * Schedule product monitoring
     * 
     * @param identifier Product identifier
     * @param frequency How often to refresh ('hourly', 'daily', 'weekly')
     * @param retailer Optional retailer to monitor
     * @return Scheduling confirmation
     * @throws ShopSavvyApiException if the API request fails
     */
    @NotNull
    public ApiResponse<ScheduleResponse> scheduleProductMonitoring(@NotNull String identifier, @NotNull String frequency, @Nullable String retailer) throws ShopSavvyApiException {
        ScheduleRequest scheduleRequest = new ScheduleRequest(identifier, frequency, retailer);
        
        try {
            String json = objectMapper.writeValueAsString(scheduleRequest);
            RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
            
            Request request = new Request.Builder()
                .url(baseUrl + "/products/schedule")
                .post(body)
                .build();
            
            return executeRequest(request, ScheduleResponse.class);
        } catch (IOException e) {
            throw new ShopSavvyApiException("Failed to serialize request", e);
        }
    }
    
    /**
     * Get all scheduled products
     * 
     * @return List of scheduled products
     * @throws ShopSavvyApiException if the API request fails
     */
    @NotNull
    public ApiResponse<List<ScheduledProduct>> getScheduledProducts() throws ShopSavvyApiException {
        Request request = new Request.Builder()
            .url(baseUrl + "/products/scheduled")
            .get()
            .build();
        
        return executeRequestForList(request, ScheduledProduct.class);
    }
    
    /**
     * Remove product from monitoring schedule
     * 
     * @param identifier Product identifier to remove
     * @return Removal confirmation
     * @throws ShopSavvyApiException if the API request fails
     */
    @NotNull
    public ApiResponse<RemoveResponse> removeProductFromSchedule(@NotNull String identifier) throws ShopSavvyApiException {
        RemoveRequest removeRequest = new RemoveRequest(identifier);
        
        try {
            String json = objectMapper.writeValueAsString(removeRequest);
            RequestBody body = RequestBody.create(json, MediaType.get("application/json"));
            
            Request request = new Request.Builder()
                .url(baseUrl + "/products/schedule")
                .delete(body)
                .build();
            
            return executeRequest(request, RemoveResponse.class);
        } catch (IOException e) {
            throw new ShopSavvyApiException("Failed to serialize request", e);
        }
    }
    
    /**
     * Get API usage information
     * 
     * @return Current usage and credit information
     * @throws ShopSavvyApiException if the API request fails
     */
    @NotNull
    public ApiResponse<UsageInfo> getUsage() throws ShopSavvyApiException {
        Request request = new Request.Builder()
            .url(baseUrl + "/usage")
            .get()
            .build();
        
        return executeRequest(request, UsageInfo.class);
    }
    
    private <T> ApiResponse<T> executeRequest(Request request, Class<T> responseType) throws ShopSavvyApiException {
        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body().string();
            
            if (!response.isSuccessful()) {
                throw createExceptionFromResponse(response.code(), responseBody);
            }
            
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            return objectMapper.readValue(responseBody,
                typeFactory.constructParametricType(ApiResponse.class, responseType));
                
        } catch (IOException e) {
            throw new ShopSavvyNetworkException("Network error: " + e.getMessage(), e);
        }
    }
    
    private <T> ApiResponse<List<T>> executeRequestForList(Request request, Class<T> itemType) throws ShopSavvyApiException {
        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body().string();
            
            if (!response.isSuccessful()) {
                throw createExceptionFromResponse(response.code(), responseBody);
            }
            
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            return objectMapper.readValue(responseBody,
                typeFactory.constructParametricType(ApiResponse.class,
                    typeFactory.constructCollectionType(List.class, itemType)));
                    
        } catch (IOException e) {
            throw new ShopSavvyNetworkException("Network error: " + e.getMessage(), e);
        }
    }
    
    private <T> ApiResponse<Map<String, List<T>>> executeRequestForMap(Request request, Class<T> itemType) throws ShopSavvyApiException {
        try (Response response = httpClient.newCall(request).execute()) {
            String responseBody = response.body().string();
            
            if (!response.isSuccessful()) {
                throw createExceptionFromResponse(response.code(), responseBody);
            }
            
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            return objectMapper.readValue(responseBody,
                typeFactory.constructParametricType(ApiResponse.class,
                    typeFactory.constructMapType(Map.class, 
                        typeFactory.constructType(String.class),
                        typeFactory.constructCollectionType(List.class, itemType))));
                        
        } catch (IOException e) {
            throw new ShopSavvyNetworkException("Network error: " + e.getMessage(), e);
        }
    }
    
    private ShopSavvyApiException createExceptionFromResponse(int statusCode, String responseBody) {
        String errorMessage = "Unknown error";
        try {
            Map<String, Object> errorResponse = objectMapper.readValue(responseBody, Map.class);
            errorMessage = (String) errorResponse.getOrDefault("error", errorMessage);
        } catch (IOException ignored) {
            errorMessage = responseBody;
        }
        
        switch (statusCode) {
            case 401:
                return new ShopSavvyAuthenticationException("Authentication failed. Check your API key.");
            case 404:
                return new ShopSavvyNotFoundException("Resource not found");
            case 422:
                return new ShopSavvyValidationException("Request validation failed. Check your parameters.");
            case 429:
                return new ShopSavvyRateLimitException("Rate limit exceeded. Please slow down your requests.");
            default:
                return new ShopSavvyApiException("HTTP " + statusCode + ": " + errorMessage);
        }
    }
    
    @Override
    public void close() {
        if (httpClient != null) {
            httpClient.dispatcher().executorService().shutdown();
            httpClient.connectionPool().evictAll();
        }
    }
}