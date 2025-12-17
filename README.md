# ShopSavvy Data API - Java SDK

[![Maven Central](https://img.shields.io/maven-central/v/com.shopsavvy/shopsavvy-sdk-java.svg)](https://search.maven.org/artifact/com.shopsavvy/shopsavvy-sdk-java)
[![Java](https://img.shields.io/badge/Java-8+-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.5+-green.svg)](https://spring.io/projects/spring-boot)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Documentation](https://img.shields.io/badge/docs-shopsavvy.com-blue)](https://shopsavvy.com/data/documentation)

Official Java SDK for the [ShopSavvy Data API](https://shopsavvy.com/data). Access comprehensive product data, real-time pricing, and historical price trends across **thousands of retailers** and **millions of products**. Built for **enterprise Java**, **Spring Boot**, **Android**, and **microservices** architectures.

## ⚡ 30-Second Quick Start

```java
<!-- Add to pom.xml: -->
<!-- <dependency>
    <groupId>com.shopsavvy</groupId>
    <artifactId>shopsavvy-sdk-java</artifactId>
    <version>1.0.0</version>
</dependency> -->

// Use in Java/Spring Boot:
import com.shopsavvy.sdk.*;
import com.shopsavvy.sdk.models.*;

public class QuickExample {
    public static void main(String[] args) {
        ShopSavvyClient client = new ShopSavvyClient("ss_live_your_api_key_here");
        
        try {
            ApiResponse<ProductDetails> product = client.getProductDetails("012345678901");
            ApiResponse<List<Offer>> offers = client.getCurrentOffers("012345678901");
            
            Offer bestOffer = offers.getData().stream()
                .min((o1, o2) -> Double.compare(o1.getPrice(), o2.getPrice()))
                .orElse(null);
            
            System.out.printf("%s - Best price: $%.2f at %s%n",
                product.getData().getName(),
                bestOffer.getPrice(),
                bestOffer.getRetailer());
                
        } catch (ShopSavvyApiException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            client.close();
        }
    }
}
```

## 📊 Feature Comparison

| Feature | Free Tier | Pro | Enterprise |
|---------|-----------|-----|-----------| 
| **API Calls/Month** | 1,000 | 100,000 | Unlimited |
| **Product Details** | ✅ | ✅ | ✅ |
| **Real-time Pricing** | ✅ | ✅ | ✅ |
| **Price History** | 30 days | 1 year | 5+ years |
| **Bulk Operations** | 10/batch | 100/batch | 1000/batch |
| **Retailer Coverage** | 50+ | 500+ | 1000+ |
| **Rate Limiting** | 60/hour | 1000/hour | Custom |
| **Support** | Community | Email | Phone + Dedicated |

## 🚀 Installation & Setup

### Requirements

- Java 8 or higher
- Maven 3.6+ or Gradle 6.0+
- Jackson 2.15+ (for JSON processing)
- OkHttp 4.11+ (for HTTP requests)

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>com.shopsavvy</groupId>
        <artifactId>shopsavvy-sdk-java</artifactId>
        <version>1.0.0</version>
    </dependency>
    
    <!-- For Spring Boot projects -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>2.7.14</version>
    </dependency>
    
    <!-- For reactive programming -->
    <dependency>
        <groupId>io.reactivex.rxjava3</groupId>
        <artifactId>rxjava</artifactId>
        <version>3.1.6</version>
    </dependency>
</dependencies>
```

### Gradle

```gradle
dependencies {
    implementation 'com.shopsavvy:shopsavvy-sdk-java:1.0.0'
    
    // For Spring Boot projects
    implementation 'org.springframework.boot:spring-boot-starter-web:2.7.14'
    
    // For reactive programming
    implementation 'io.reactivex.rxjava3:rxjava:3.1.6'
}
```

### Get Your API Key

1. **Sign up**: Visit [shopsavvy.com/data](https://shopsavvy.com/data)
2. **Choose plan**: Select based on your usage needs  
3. **Get API key**: Copy from your dashboard
4. **Test**: Run the 30-second example above

## 📖 Complete API Reference

### Client Configuration

```java
import com.shopsavvy.sdk.*;
import com.shopsavvy.sdk.exceptions.*;

// Basic configuration
ShopSavvyClient client = new ShopSavvyClient("ss_live_your_api_key_here");

// Advanced configuration
ShopSavvyClient client = new ShopSavvyClient.Builder()
    .apiKey("ss_live_your_api_key_here")
    .baseUrl("https://api.shopsavvy.com/v1")     // Custom base URL
    .timeoutSeconds(60)                          // Request timeout
    .retryAttempts(3)                           // Retry failed requests
    .userAgent("MyApp/1.0.0")                   // Custom user agent
    .connectionPoolSize(10)                     // Connection pool size
    .build();

// Environment variable configuration
String apiKey = System.getenv("SHOPSAVVY_API_KEY");
if (apiKey == null) {
    throw new IllegalStateException("SHOPSAVVY_API_KEY environment variable not set");
}
ShopSavvyClient client = new ShopSavvyClient(apiKey);
```

### Product Lookup

#### Single Product
```java
import com.shopsavvy.sdk.models.*;

// Look up by barcode, ASIN, URL, model number, or ShopSavvy ID
ApiResponse<ProductDetails> product = client.getProductDetails("012345678901");
ApiResponse<ProductDetails> amazonProduct = client.getProductDetails("B08N5WRWNW");
ApiResponse<ProductDetails> urlProduct = client.getProductDetails("https://www.amazon.com/dp/B08N5WRWNW");
ApiResponse<ProductDetails> modelProduct = client.getProductDetails("MQ023LL/A"); // iPhone model number

ProductDetails productData = product.getData();
System.out.println("📦 Product: " + productData.getName());
System.out.println("🏷️ Brand: " + (productData.getBrand() != null ? productData.getBrand() : "N/A"));
System.out.println("📂 Category: " + (productData.getCategory() != null ? productData.getCategory() : "N/A"));
System.out.println("🔢 Product ID: " + productData.getId());

if (productData.getAsin() != null) {
    System.out.println("📦 ASIN: " + productData.getAsin());
}
if (productData.getModelNumber() != null) {
    System.out.println("🔧 Model: " + productData.getModelNumber());
}
```

#### Bulk Product Lookup
```java
import java.util.*;

// Process up to 100 products at once (Pro plan)
List<String> identifiers = Arrays.asList(
    "012345678901", "B08N5WRWNW", "045496590048",
    "https://www.bestbuy.com/site/product/123456",
    "MQ023LL/A", "SM-S911U"  // iPhone and Samsung model numbers
);

ApiResponse<List<ProductDetails>> products = client.getProductDetailsBatch(identifiers);

for (int i = 0; i < identifiers.size(); i++) {
    ProductDetails product = products.getData().get(i);
    if (product == null) {
        System.out.println("❌ Failed to find product: " + identifiers.get(i));
    } else {
        System.out.printf("✓ Found: %s by %s%n", 
            product.getName(), 
            product.getBrand() != null ? product.getBrand() : "Unknown");
    }
}
```

### Real-Time Pricing

#### Spring Boot REST API Integration
```java
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.cache.annotation.Cacheable;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping("/{identifier}/compare")
    public ResponseEntity<PriceComparisonResponse> comparePrice(@PathVariable String identifier) {
        try {
            PriceComparisonResponse comparison = productService.comparePrice(identifier);
            return ResponseEntity.ok(comparison);
        } catch (ShopSavvyApiException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
    @GetMapping("/{identifier}/history")
    public ResponseEntity<List<OfferWithHistory>> getPriceHistory(
        @PathVariable String identifier,
        @RequestParam String startDate,
        @RequestParam String endDate) {
        
        try {
            List<OfferWithHistory> history = productService.getPriceHistory(identifier, startDate, endDate);
            return ResponseEntity.ok(history);
        } catch (ShopSavvyApiException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
    @PostMapping("/{identifier}/alerts")
    public ResponseEntity<String> setPriceAlert(
        @PathVariable String identifier,
        @RequestBody PriceAlertRequest request) {
        
        try {
            productService.setPriceAlert(identifier, request.getTargetPrice(), request.getEmail());
            return ResponseEntity.ok("Price alert set successfully");
        } catch (ShopSavvyApiException e) {
            return ResponseEntity.status(500).body("Failed to set price alert");
        }
    }
}

@Service
public class ProductService {
    
    private final ShopSavvyClient client;
    private final EmailService emailService;
    
    public ProductService(ShopSavvyClient client, EmailService emailService) {
        this.client = client;
        this.emailService = emailService;
    }
    
    @Cacheable(value = "priceComparisons", key = "#identifier")
    public PriceComparisonResponse comparePrice(String identifier) throws ShopSavvyApiException {
        ApiResponse<List<Offer>> response = client.getCurrentOffers(identifier);
        List<Offer> offers = response.getData();
        
        if (offers.isEmpty()) {
            throw new ShopSavvyNotFoundException("No offers found for product: " + identifier);
        }
        
        // Sort by price
        List<Offer> sortedOffers = offers.stream()
            .filter(offer -> offer.getPrice() != null)
            .sorted((o1, o2) -> Double.compare(o1.getPrice(), o2.getPrice()))
            .collect(Collectors.toList());
        
        Offer cheapest = sortedOffers.get(0);
        Offer mostExpensive = sortedOffers.get(sortedOffers.size() - 1);
        
        double averagePrice = sortedOffers.stream()
            .mapToDouble(Offer::getPrice)
            .average()
            .orElse(0.0);
        
        double potentialSavings = mostExpensive.getPrice() - cheapest.getPrice();
        
        // Filter by availability and condition
        long inStockCount = offers.stream()
            .filter(offer -> "in_stock".equals(offer.getAvailability()))
            .count();
        
        long newConditionCount = offers.stream()
            .filter(offer -> "new".equals(offer.getCondition()))
            .count();
        
        return new PriceComparisonResponse(
            sortedOffers,
            cheapest,
            mostExpensive,
            averagePrice,
            potentialSavings,
            (int) inStockCount,
            (int) newConditionCount
        );
    }
    
    public List<OfferWithHistory> getPriceHistory(String identifier, String startDate, String endDate) throws ShopSavvyApiException {
        ApiResponse<List<OfferWithHistory>> response = client.getPriceHistory(identifier, startDate, endDate);
        return response.getData();
    }
    
    public void setPriceAlert(String identifier, double targetPrice, String email) throws ShopSavvyApiException {
        // Schedule monitoring with API
        client.scheduleProductMonitoring(identifier, "daily");
        
        // Set up local alert tracking (you would implement this with your database)
        PriceAlert alert = new PriceAlert(identifier, targetPrice, email, true);
        // saveToDatabase(alert);
        
        // Send confirmation email
        emailService.sendPriceAlertConfirmation(email, identifier, targetPrice);
    }
}

// Response DTOs
public class PriceComparisonResponse {
    private List<Offer> offers;
    private Offer bestOffer;
    private Offer worstOffer;
    private double averagePrice;
    private double potentialSavings;
    private int inStockOffers;
    private int newConditionOffers;
    
    // Constructor and getters/setters
    public PriceComparisonResponse(List<Offer> offers, Offer bestOffer, Offer worstOffer,
                                 double averagePrice, double potentialSavings, 
                                 int inStockOffers, int newConditionOffers) {
        this.offers = offers;
        this.bestOffer = bestOffer;
        this.worstOffer = worstOffer;
        this.averagePrice = averagePrice;
        this.potentialSavings = potentialSavings;
        this.inStockOffers = inStockOffers;
        this.newConditionOffers = newConditionOffers;
    }
    
    // Getters and setters...
}

public class PriceAlertRequest {
    private double targetPrice;
    private String email;
    
    // Getters and setters...
}
```

#### Enterprise Background Monitoring Service
```java
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.*;
import java.util.List;

@Component
public class PriceMonitoringService {
    
    private final ShopSavvyClient client;
    private final PriceAlertRepository alertRepository;
    private final NotificationService notificationService;
    private final ExecutorService executorService;
    
    public PriceMonitoringService(ShopSavvyClient client, 
                                PriceAlertRepository alertRepository,
                                NotificationService notificationService) {
        this.client = client;
        this.alertRepository = alertRepository;
        this.notificationService = notificationService;
        this.executorService = Executors.newFixedThreadPool(10);
    }
    
    @Scheduled(fixedRate = 3600000) // Run every hour
    @Transactional
    public void monitorPrices() {
        List<PriceAlert> activeAlerts = alertRepository.findByActiveTrue();
        
        List<CompletableFuture<Void>> futures = activeAlerts.stream()
            .map(alert -> CompletableFuture.runAsync(() -> checkPriceAlert(alert), executorService))
            .collect(Collectors.toList());
        
        // Wait for all checks to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenRun(() -> System.out.println("Completed monitoring " + activeAlerts.size() + " price alerts"))
            .exceptionally(throwable -> {
                System.err.println("Error during price monitoring: " + throwable.getMessage());
                return null;
            });
    }
    
    private void checkPriceAlert(PriceAlert alert) {
        try {
            ApiResponse<List<Offer>> response = client.getCurrentOffers(alert.getProductId());
            List<Offer> offers = response.getData();
            
            Offer bestOffer = offers.stream()
                .filter(offer -> offer.getPrice() != null)
                .min((o1, o2) -> Double.compare(o1.getPrice(), o2.getPrice()))
                .orElse(null);
            
            if (bestOffer != null && bestOffer.getPrice() <= alert.getTargetPrice()) {
                // Send notification
                notificationService.sendPriceAlert(alert, bestOffer);
                
                // Update alert
                alert.setLastTriggered(Instant.now());
                alert.setTriggeredCount(alert.getTriggeredCount() + 1);
                alertRepository.save(alert);
                
                System.out.printf("🎯 Price alert triggered! Product %s reached target price $%.2f at %s%n",
                    alert.getProductId(), bestOffer.getPrice(), bestOffer.getRetailer());
            }
            
            // Update price history
            savePriceHistory(alert.getProductId(), bestOffer);
            
            // Rate limiting
            Thread.sleep(1000);
            
        } catch (ShopSavvyApiException e) {
            System.err.println("Failed to check price for " + alert.getProductId() + ": " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void savePriceHistory(String productId, Offer offer) {
        if (offer != null) {
            PriceHistory history = new PriceHistory(
                productId,
                offer.getRetailer(),
                offer.getPrice(),
                Instant.now()
            );
            // Save to database
            // priceHistoryRepository.save(history);
        }
    }
}

// Notification Service
@Service
public class NotificationService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendPriceAlert(PriceAlert alert, Offer offer) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(alert.getEmail());
            message.setSubject("🎯 Price Alert Triggered!");
            message.setText(String.format(
                "Great news! The product you're watching has reached your target price.\n\n" +
                "Product ID: %s\n" +
                "Target Price: $%.2f\n" +
                "Current Price: $%.2f\n" +
                "Retailer: %s\n" +
                "Savings: $%.2f\n\n" +
                "Shop now: %s\n\n" +
                "Happy shopping!\n" +
                "The ShopSavvy Team",
                alert.getProductId(),
                alert.getTargetPrice(),
                offer.getPrice(),
                offer.getRetailer(),
                alert.getTargetPrice() - offer.getPrice(),
                offer.getUrl() != null ? offer.getUrl() : "Visit retailer website"
            ));
            
            mailSender.send(message);
            
        } catch (Exception e) {
            System.err.println("Failed to send price alert email: " + e.getMessage());
        }
    }
}
```

## 🚀 Production Deployment

### Enterprise Spring Boot Configuration

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableCaching
@EnableScheduling
@EnableRetry
@ConfigurationProperties(prefix = "shopsavvy")
public class ShopSavvyConfig {
    
    private String apiKey;
    private String baseUrl = "https://api.shopsavvy.com/v1";
    private int timeoutSeconds = 60;
    private int retryAttempts = 3;
    private int connectionPoolSize = 20;
    
    @Bean
    public ShopSavvyClient shopSavvyClient() {
        return new ShopSavvyClient.Builder()
            .apiKey(apiKey)
            .baseUrl(baseUrl)
            .timeoutSeconds(timeoutSeconds)
            .retryAttempts(retryAttempts)
            .connectionPoolSize(connectionPoolSize)
            .build();
    }
    
    // Getters and setters for configuration properties
    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    
    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
    
    public int getTimeoutSeconds() { return timeoutSeconds; }
    public void setTimeoutSeconds(int timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
    
    public int getRetryAttempts() { return retryAttempts; }
    public void setRetryAttempts(int retryAttempts) { this.retryAttempts = retryAttempts; }
    
    public int getConnectionPoolSize() { return connectionPoolSize; }
    public void setConnectionPoolSize(int connectionPoolSize) { this.connectionPoolSize = connectionPoolSize; }
}

// Application properties
// application.yml
/*
shopsavvy:
  api-key: ${SHOPSAVVY_API_KEY:ss_live_your_api_key_here}
  base-url: ${SHOPSAVVY_BASE_URL:https://api.shopsavvy.com/v1}
  timeout-seconds: ${SHOPSAVVY_TIMEOUT:60}
  retry-attempts: ${SHOPSAVVY_RETRY_ATTEMPTS:3}
  connection-pool-size: ${SHOPSAVVY_POOL_SIZE:20}

spring:
  cache:
    type: caffeine
    caffeine:
      spec: maximumSize=1000,expireAfterWrite=5m
  
  mail:
    host: ${MAIL_HOST:smtp.gmail.com}
    port: ${MAIL_PORT:587}
    username: ${MAIL_USERNAME}
    password: ${MAIL_PASSWORD}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
*/
```

### Reactive Programming with RxJava
```java
import io.reactivex.rxjava3.core.*;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class ReactiveProductService {
    
    private final ShopSavvyClient client;
    
    public ReactiveProductService(ShopSavvyClient client) {
        this.client = client;
    }
    
    public Observable<PriceUpdate> monitorPriceChanges(String productId, Duration interval) {
        return Observable.interval(interval.toSeconds(), TimeUnit.SECONDS)
            .map(tick -> getCurrentPrice(productId))
            .filter(priceUpdate -> priceUpdate != null)
            .distinctUntilChanged(PriceUpdate::getPrice)
            .observeOn(Schedulers.io());
    }
    
    public Single<List<ProductDetails>> searchProductsReactive(List<String> identifiers) {
        return Single.fromCallable(() -> {
            try {
                ApiResponse<List<ProductDetails>> response = client.getProductDetailsBatch(identifiers);
                return response.getData();
            } catch (ShopSavvyApiException e) {
                throw new RuntimeException(e);
            }
        }).subscribeOn(Schedulers.io());
    }
    
    public Observable<PriceComparison> streamPriceComparisons(List<String> productIds) {
        return Observable.fromIterable(productIds)
            .flatMap(productId -> 
                Observable.fromCallable(() -> comparePrice(productId))
                    .subscribeOn(Schedulers.io())
                    .retry(3)
                    .timeout(30, TimeUnit.SECONDS)
                    .onErrorResumeNext(throwable -> {
                        System.err.println("Failed to get price for " + productId + ": " + throwable.getMessage());
                        return Observable.empty();
                    })
            );
    }
    
    private PriceUpdate getCurrentPrice(String productId) {
        try {
            ApiResponse<List<Offer>> response = client.getCurrentOffers(productId);
            Offer bestOffer = response.getData().stream()
                .min((o1, o2) -> Double.compare(o1.getPrice(), o2.getPrice()))
                .orElse(null);
            
            if (bestOffer != null) {
                return new PriceUpdate(productId, bestOffer.getPrice(), bestOffer.getRetailer(), Instant.now());
            }
        } catch (ShopSavvyApiException e) {
            System.err.println("Error getting price for " + productId + ": " + e.getMessage());
        }
        return null;
    }
    
    private PriceComparison comparePrice(String productId) throws ShopSavvyApiException {
        // Implementation similar to previous example
        ApiResponse<List<Offer>> response = client.getCurrentOffers(productId);
        // ... rest of implementation
        return new PriceComparison(/* ... */);
    }
}

// Usage in controller
@RestController
public class ReactiveProductController {
    
    @Autowired
    private ReactiveProductService productService;
    
    @GetMapping(value = "/api/v1/products/{id}/price-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Observable<PriceUpdate> streamPriceUpdates(@PathVariable String id) {
        return productService.monitorPriceChanges(id, Duration.ofMinutes(5))
            .doOnNext(update -> System.out.println("Price update: " + update))
            .doOnError(error -> System.err.println("Stream error: " + error.getMessage()));
    }
}
```

### Microservices Integration
```java
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

// Feign client for microservices communication
@FeignClient(name = "product-service", url = "${product-service.url}")
public interface ProductServiceClient {
    
    @GetMapping("/api/v1/products/{id}")
    ResponseEntity<ProductDetails> getProduct(@PathVariable String id);
    
    @GetMapping("/api/v1/products/{id}/offers")
    ResponseEntity<List<Offer>> getOffers(@PathVariable String id);
}

// Circuit breaker integration
@Component
public class ProductServiceFallback {
    
    private final ShopSavvyClient client;
    
    public ProductServiceFallback(ShopSavvyClient client) {
        this.client = client;
    }
    
    @CircuitBreaker(name = "product-service", fallbackMethod = "fallbackGetProduct")
    public ProductDetails getProductWithFallback(String productId) throws ShopSavvyApiException {
        // Primary service call
        return callPrimaryProductService(productId);
    }
    
    public ProductDetails fallbackGetProduct(String productId, Exception ex) {
        try {
            // Fallback to ShopSavvy API
            ApiResponse<ProductDetails> response = client.getProductDetails(productId);
            return response.getData();
        } catch (ShopSavvyApiException e) {
            throw new RuntimeException("Both primary service and fallback failed", e);
        }
    }
    
    private ProductDetails callPrimaryProductService(String productId) {
        // Implementation to call primary service
        return null; // placeholder
    }
}
```

## Exception Handling

The SDK provides comprehensive exception handling with Java-specific patterns:

```java
import com.shopsavvy.sdk.exceptions.*;

public void handleProductLookup(String identifier) {
    try {
        ApiResponse<ProductDetails> response = client.getProductDetails(identifier);
        System.out.println("✅ Found product: " + response.getData().getName());
        
    } catch (ShopSavvyAuthenticationException e) {
        System.err.println("🔐 Authentication failed: " + e.getMessage());
        // Refresh API key or redirect to login
        
    } catch (ShopSavvyNotFoundException e) {
        System.err.println("❌ Product not found: " + e.getMessage());
        // Show "not found" UI
        
    } catch (ShopSavvyValidationException e) {
        System.err.println("⚠️ Invalid parameters: " + e.getMessage());
        // Show validation error to user
        
    } catch (ShopSavvyRateLimitException e) {
        System.err.println("🚦 Rate limit exceeded: " + e.getMessage());
        // Implement exponential backoff
        try {
            Thread.sleep(e.getRetryAfterSeconds() * 1000L);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        
    } catch (ShopSavvyNetworkException e) {
        System.err.println("🌐 Network error: " + e.getMessage());
        // Show offline mode or retry option
        
    } catch (ShopSavvyApiException e) {
        System.err.println("❌ API error: " + e.getMessage());
        // Generic error handling
        
    } catch (Exception e) {
        System.err.println("💥 Unexpected error: " + e.getMessage());
        // Log to crash reporting service
    }
}
```

### Retry Logic with Exponential Backoff

```java
import java.time.Duration;
import java.util.function.Supplier;

public class RetryUtil {
    
    public static <T> T retryWithBackoff(Supplier<T> operation, 
                                       int maxAttempts, 
                                       Duration initialDelay, 
                                       Duration maxDelay, 
                                       double multiplier) throws Exception {
        
        Exception lastException = null;
        Duration currentDelay = initialDelay;
        
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return operation.get();
                
            } catch (ShopSavvyRateLimitException e) {
                lastException = e;
                if (attempt == maxAttempts) break;
                
                // Use server-specified retry delay if available
                Duration retryDelay = e.getRetryAfterSeconds() != null 
                    ? Duration.ofSeconds(e.getRetryAfterSeconds())
                    : currentDelay;
                
                try {
                    Thread.sleep(retryDelay.toMillis());
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ie);
                }
                
                currentDelay = Duration.ofMillis(Math.min(
                    (long) (currentDelay.toMillis() * multiplier),
                    maxDelay.toMillis()
                ));
                
            } catch (ShopSavvyNetworkException e) {
                lastException = e;
                if (attempt == maxAttempts) break;
                
                try {
                    Thread.sleep(currentDelay.toMillis());
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ie);
                }
                
                currentDelay = Duration.ofMillis(Math.min(
                    (long) (currentDelay.toMillis() * multiplier),
                    maxDelay.toMillis()
                ));
            }
        }
        
        throw new RuntimeException("Operation failed after " + maxAttempts + " attempts", lastException);
    }
}

// Usage
public ProductDetails getProductWithRetry(String identifier) throws Exception {
    return RetryUtil.retryWithBackoff(
        () -> {
            try {
                return client.getProductDetails(identifier).getData();
            } catch (ShopSavvyApiException e) {
                throw new RuntimeException(e);
            }
        },
        3,                              // max attempts
        Duration.ofSeconds(1),          // initial delay
        Duration.ofSeconds(30),         // max delay
        2.0                            // multiplier
    );
}
```

## 🛠️ Development & Testing

### Local Development Setup

```bash
# Clone the repository
git clone https://github.com/shopsavvy/sdk-java.git
cd sdk-java

# Build with Maven
mvn clean compile

# Run tests
mvn test

# Build JAR
mvn package

# Install to local repository
mvn install

# Build with Gradle (alternative)
./gradlew build
./gradlew test
./gradlew publishToMavenLocal
```

### Testing Your Integration

```java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class SDKIntegrationTest {
    
    private ShopSavvyClient client;
    
    @BeforeEach
    void setUp() {
        client = new ShopSavvyClient("ss_test_your_test_key_here");
    }
    
    @Test
    void testProductLookup() {
        try {
            ApiResponse<ProductDetails> response = client.getProductDetails("012345678901");
            assertNotNull(response.getData());
            assertNotNull(response.getData().getName());
            System.out.println("✅ Product lookup: " + response.getData().getName());
            
        } catch (ShopSavvyApiException e) {
            fail("Product lookup failed: " + e.getMessage());
        }
    }
    
    @Test
    void testCurrentOffers() {
        try {
            ApiResponse<List<Offer>> response = client.getCurrentOffers("012345678901");
            assertNotNull(response.getData());
            System.out.println("✅ Current offers: " + response.getData().size() + " found");
            
        } catch (ShopSavvyApiException e) {
            fail("Current offers failed: " + e.getMessage());
        }
    }
    
    @Test
    void testUsageInfo() {
        try {
            ApiResponse<UsageInfo> response = client.getUsage();
            assertNotNull(response.getData());
            System.out.println("✅ API usage: " + response.getData().getCreditsRemaining() + " credits remaining");
            
        } catch (ShopSavvyApiException e) {
            fail("Usage info failed: " + e.getMessage());
        }
    }
    
    @AfterEach
    void tearDown() {
        if (client != null) {
            client.close();
        }
        System.out.println("\n🎉 All tests passed! SDK is working correctly.");
    }
}
```

## Data Models

All models are implemented as Java POJOs with Jackson annotations and null-safety:

### ProductDetails
```java
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductDetails {
    
    @JsonProperty("id")
    @NotNull
    private String id;
    
    @JsonProperty("name")
    @NotNull
    private String name;
    
    @JsonProperty("description")
    @Nullable
    private String description;
    
    @JsonProperty("brand")
    @Nullable
    private String brand;
    
    @JsonProperty("category")
    @Nullable
    private String category;
    
    @JsonProperty("upc")
    @Nullable
    private String upc;
    
    @JsonProperty("asin")
    @Nullable
    private String asin;
    
    @JsonProperty("model_number")
    @Nullable
    private String modelNumber;
    
    @JsonProperty("images")
    @NotNull
    private List<String> images = new ArrayList<>();
    
    @JsonProperty("specifications")
    @NotNull
    private Map<String, String> specifications = new HashMap<>();
    
    @JsonProperty("created_at")
    @Nullable
    private String createdAt;
    
    @JsonProperty("updated_at")
    @Nullable
    private String updatedAt;
    
    // Constructors
    public ProductDetails() {}
    
    public ProductDetails(@NotNull String id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }
    
    // Computed properties for convenience
    public boolean hasImages() {
        return images != null && !images.isEmpty();
    }
    
    public boolean hasSpecifications() {
        return specifications != null && !specifications.isEmpty();
    }
    
    public String getDisplayName() {
        if (brand != null && !brand.trim().isEmpty()) {
            return brand.trim() + " " + name.trim();
        }
        return name.trim();
    }
    
    // Standard getters and setters
    @NotNull
    public String getId() { return id; }
    public void setId(@NotNull String id) { this.id = id; }
    
    @NotNull
    public String getName() { return name; }
    public void setName(@NotNull String name) { this.name = name; }
    
    @Nullable
    public String getDescription() { return description; }
    public void setDescription(@Nullable String description) { this.description = description; }
    
    @Nullable
    public String getBrand() { return brand; }
    public void setBrand(@Nullable String brand) { this.brand = brand; }
    
    @Nullable
    public String getCategory() { return category; }
    public void setCategory(@Nullable String category) { this.category = category; }
    
    @Nullable
    public String getUpc() { return upc; }
    public void setUpc(@Nullable String upc) { this.upc = upc; }
    
    @Nullable
    public String getAsin() { return asin; }
    public void setAsin(@Nullable String asin) { this.asin = asin; }
    
    @Nullable
    public String getModelNumber() { return modelNumber; }
    public void setModelNumber(@Nullable String modelNumber) { this.modelNumber = modelNumber; }
    
    @NotNull
    public List<String> getImages() { return images; }
    public void setImages(@NotNull List<String> images) { this.images = images; }
    
    @NotNull
    public Map<String, String> getSpecifications() { return specifications; }
    public void setSpecifications(@NotNull Map<String, String> specifications) { this.specifications = specifications; }
    
    @Nullable
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(@Nullable String createdAt) { this.createdAt = createdAt; }
    
    @Nullable
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(@Nullable String updatedAt) { this.updatedAt = updatedAt; }
    
    @Override
    public String toString() {
        return "ProductDetails{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
```

### Offer
```java
@JsonIgnoreProperties(ignoreUnknown = true)
public class Offer {
    
    @JsonProperty("retailer")
    @NotNull
    private String retailer;
    
    @JsonProperty("price")
    @Nullable
    private Double price;
    
    @JsonProperty("currency")
    @Nullable
    private String currency = "USD";
    
    @JsonProperty("availability")
    @Nullable
    private String availability;
    
    @JsonProperty("condition")
    @Nullable
    private String condition;
    
    @JsonProperty("shipping_cost")
    @Nullable
    private Double shippingCost;
    
    @JsonProperty("url")
    @Nullable
    private String url;
    
    @JsonProperty("last_updated")
    @Nullable
    private String lastUpdated;
    
    // Constructors
    public Offer() {}
    
    public Offer(@NotNull String retailer, @Nullable Double price) {
        this.retailer = retailer;
        this.price = price;
    }
    
    // Computed properties
    public boolean isInStock() {
        return "in_stock".equals(availability);
    }
    
    public boolean isNew() {
        return "new".equals(condition);
    }
    
    public double getTotalCost() {
        double basePrice = price != null ? price : 0.0;
        double shipping = shippingCost != null ? shippingCost : 0.0;
        return basePrice + shipping;
    }
    
    public String getFormattedPrice() {
        if (price == null) return "N/A";
        return String.format("$%.2f", price);
    }
    
    // Standard getters and setters
    @NotNull
    public String getRetailer() { return retailer; }
    public void setRetailer(@NotNull String retailer) { this.retailer = retailer; }
    
    @Nullable
    public Double getPrice() { return price; }
    public void setPrice(@Nullable Double price) { this.price = price; }
    
    @Nullable
    public String getCurrency() { return currency; }
    public void setCurrency(@Nullable String currency) { this.currency = currency; }
    
    @Nullable
    public String getAvailability() { return availability; }
    public void setAvailability(@Nullable String availability) { this.availability = availability; }
    
    @Nullable
    public String getCondition() { return condition; }
    public void setCondition(@Nullable String condition) { this.condition = condition; }
    
    @Nullable
    public Double getShippingCost() { return shippingCost; }
    public void setShippingCost(@Nullable Double shippingCost) { this.shippingCost = shippingCost; }
    
    @Nullable
    public String getUrl() { return url; }
    public void setUrl(@Nullable String url) { this.url = url; }
    
    @Nullable
    public String getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(@Nullable String lastUpdated) { this.lastUpdated = lastUpdated; }
    
    @Override
    public String toString() {
        return "Offer{" +
                "retailer='" + retailer + '\'' +
                ", price=" + price +
                ", availability='" + availability + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }
}
```

### UsageInfo
```java
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsageInfo {
    
    @JsonProperty("credits_used")
    @Nullable
    private Integer creditsUsed;
    
    @JsonProperty("credits_remaining")
    @Nullable
    private Integer creditsRemaining;
    
    @JsonProperty("credits_limit")
    @Nullable
    private Integer creditsLimit;
    
    @JsonProperty("reset_date")
    @Nullable
    private String resetDate;
    
    @JsonProperty("current_period_start")
    @Nullable
    private String currentPeriodStart;
    
    @JsonProperty("current_period_end")
    @Nullable
    private String currentPeriodEnd;
    
    // Constructors
    public UsageInfo() {}
    
    // Computed properties
    public double getUsagePercentage() {
        int used = creditsUsed != null ? creditsUsed : 0;
        int limit = creditsLimit != null ? creditsLimit : 1;
        return ((double) used / limit) * 100.0;
    }
    
    public boolean isNearLimit() {
        return getUsagePercentage() > 80.0;
    }
    
    // Standard getters and setters
    @Nullable
    public Integer getCreditsUsed() { return creditsUsed; }
    public void setCreditsUsed(@Nullable Integer creditsUsed) { this.creditsUsed = creditsUsed; }
    
    @Nullable
    public Integer getCreditsRemaining() { return creditsRemaining; }
    public void setCreditsRemaining(@Nullable Integer creditsRemaining) { this.creditsRemaining = creditsRemaining; }
    
    @Nullable
    public Integer getCreditsLimit() { return creditsLimit; }
    public void setCreditsLimit(@Nullable Integer creditsLimit) { this.creditsLimit = creditsLimit; }
    
    @Nullable
    public String getResetDate() { return resetDate; }
    public void setResetDate(@Nullable String resetDate) { this.resetDate = resetDate; }
    
    @Nullable
    public String getCurrentPeriodStart() { return currentPeriodStart; }
    public void setCurrentPeriodStart(@Nullable String currentPeriodStart) { this.currentPeriodStart = currentPeriodStart; }
    
    @Nullable
    public String getCurrentPeriodEnd() { return currentPeriodEnd; }
    public void setCurrentPeriodEnd(@Nullable String currentPeriodEnd) { this.currentPeriodEnd = currentPeriodEnd; }
    
    @Override
    public String toString() {
        return "UsageInfo{" +
                "creditsUsed=" + creditsUsed +
                ", creditsRemaining=" + creditsRemaining +
                ", creditsLimit=" + creditsLimit +
                ", usagePercentage=" + String.format("%.1f%%", getUsagePercentage()) +
                '}';
    }
}
```

## 📚 Additional Resources

- **[ShopSavvy Data API Documentation](https://shopsavvy.com/data/documentation)** - Complete API reference
- **[API Dashboard](https://shopsavvy.com/data/dashboard)** - Manage your API keys and usage
- **[GitHub Repository](https://github.com/shopsavvy/sdk-java)** - Source code and issues
- **[Maven Central](https://search.maven.org/artifact/com.shopsavvy/shopsavvy-sdk-java)** - Package releases
- **[Spring Boot Documentation](https://spring.io/projects/spring-boot)** - Spring Boot framework guide
- **[Java Documentation](https://docs.oracle.com/en/java/)** - Java language reference
- **[Support](mailto:business@shopsavvy.com)** - Get help from our team

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for details on:

- Reporting bugs and feature requests
- Setting up development environment  
- Submitting pull requests
- Code standards and testing
- Java and Spring Boot best practices

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🏢 About ShopSavvy

**ShopSavvy** is the world's first mobile shopping app, helping consumers find the best deals since 2008. With over **40 million downloads** and millions of active users, ShopSavvy has saved consumers billions of dollars.

### Our Data API Powers:
- 🛒 **E-commerce platforms** with competitive intelligence  
- 📊 **Market research** with real-time pricing data
- 🏪 **Retailers** with inventory and pricing optimization
- 📱 **Mobile apps** with product lookup and price comparison
- 🤖 **Business intelligence** with automated price monitoring

### Why Choose ShopSavvy Data API?
- ✅ **Trusted by millions** - Proven at scale since 2008
- ✅ **Comprehensive coverage** - 1000+ retailers, millions of products  
- ✅ **Real-time accuracy** - Fresh data updated continuously
- ✅ **Developer-friendly** - Easy integration, great documentation
- ✅ **Reliable infrastructure** - 99.9% uptime, enterprise-grade
- ✅ **Flexible pricing** - Plans for every use case and budget

### Perfect for Java & Enterprise:
- 🚀 **Spring Boot ready** - First-class Spring framework integration
- 🏢 **Enterprise patterns** - Circuit breakers, retry logic, monitoring
- 📊 **Reactive programming** - RxJava and reactive streams support
- 🛡️ **Type-safe** - Comprehensive null-safety with annotations
- ⚡ **High performance** - Connection pooling and async operations
- 🔧 **Microservices** - Perfect for distributed architectures

---

**Ready to get started?** [Sign up for your API key](https://shopsavvy.com/data) • **Need help?** [Contact us](mailto:business@shopsavvy.com)