# ShopSavvy Java SDK

Official Java SDK for ShopSavvy Data API - Access product data, pricing, and price history across thousands of retailers and millions of products.

## Installation

### Maven

Add this dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>com.shopsavvy</groupId>
    <artifactId>shopsavvy-sdk-java</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

Add this dependency to your `build.gradle`:

```gradle
implementation 'com.shopsavvy:shopsavvy-sdk-java:1.0.0'
```

## Usage

First, get your API key from [ShopSavvy Data API](https://shopsavvy.com/data).

```java
import com.shopsavvy.sdk.ShopSavvyClient;
import com.shopsavvy.sdk.models.*;
import com.shopsavvy.sdk.exceptions.*;

// Initialize the client
ShopSavvyClient client = new ShopSavvyClient("ss_live_your_api_key_here");

try {
    // Get product details
    ApiResponse<ProductDetails> productResponse = client.getProductDetails("012345678901");
    ProductDetails product = productResponse.getData();
    System.out.println("Product: " + product.getName());
    
    // Get current offers
    ApiResponse<List<Offer>> offersResponse = client.getCurrentOffers("012345678901");
    List<Offer> offers = offersResponse.getData();
    for (Offer offer : offers) {
        System.out.println(offer.getRetailer() + ": $" + offer.getPrice());
    }
    
    // Get price history
    ApiResponse<List<OfferWithHistory>> historyResponse = 
        client.getPriceHistory("012345678901", "2024-01-01", "2024-12-31");
    
    // Schedule monitoring
    ApiResponse<ScheduleResponse> scheduleResponse = 
        client.scheduleProductMonitoring("012345678901", "daily");
    
    // Check API usage
    ApiResponse<UsageInfo> usageResponse = client.getUsage();
    UsageInfo usage = usageResponse.getData();
    System.out.println("Credits remaining: " + usage.getCreditsRemaining());
    
} catch (ShopSavvyApiException e) {
    System.err.println("API Error: " + e.getMessage());
} finally {
    // Always close the client when done
    client.close();
}
```

## API Methods

### Product Details
- `getProductDetails(identifier)` - Get details for a single product
- `getProductDetailsBatch(identifiers)` - Get details for multiple products

### Current Offers
- `getCurrentOffers(identifier)` - Get current offers for a product
- `getCurrentOffersBatch(identifiers)` - Get current offers for multiple products

### Price History
- `getPriceHistory(identifier, startDate, endDate)` - Get price history for a product

### Monitoring
- `scheduleProductMonitoring(identifier, frequency)` - Schedule product monitoring
- `getScheduledProducts()` - Get all scheduled products
- `removeProductFromSchedule(identifier)` - Remove product from monitoring

### Usage
- `getUsage()` - Get API usage information

## Exception Handling

The SDK provides specific exception types for different error conditions:

- `ShopSavvyApiException` - Base exception for all API errors
- `ShopSavvyAuthenticationException` - Invalid API key
- `ShopSavvyNotFoundException` - Product not found
- `ShopSavvyValidationException` - Invalid request parameters
- `ShopSavvyRateLimitException` - Rate limit exceeded
- `ShopSavvyNetworkException` - Network connectivity issues

## Configuration

You can customize the client configuration:

```java
// Custom base URL and timeout
ShopSavvyClient client = new ShopSavvyClient(
    "ss_live_your_api_key_here",
    "https://api.shopsavvy.com/v1",
    60  // timeout in seconds
);
```

## Requirements

- Java 8 or higher
- Jackson for JSON processing
- OkHttp for HTTP requests

## License

MIT License - see [LICENSE](LICENSE) file for details.

## Support

For support, please visit [ShopSavvy Data API Documentation](https://shopsavvy.com/data) or contact business@shopsavvy.com.