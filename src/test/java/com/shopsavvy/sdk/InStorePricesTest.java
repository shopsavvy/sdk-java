package com.shopsavvy.sdk;

import com.shopsavvy.sdk.models.ApiResponse;
import com.shopsavvy.sdk.models.InStorePrice;
import com.shopsavvy.sdk.models.ProductWithOffers;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GET /products/offers carries, per product, an in_store_prices array: shelf prices read from
 * shoppers' scanned receipts, labeled source "user-collected". Parsed through the real client.
 */
class InStorePricesTest {
    private MockWebServer server;
    private ShopSavvyClient client;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        client = new ShopSavvyClient("ss_test_fixturekey123", server.url("/v1").toString(), 5);
    }

    @AfterEach
    void tearDown() throws IOException {
        client.close();
        server.shutdown();
    }

    private static String fixture() throws IOException {
        try (InputStream in = InStorePricesTest.class.getResourceAsStream("/offers-in-store-response.json")) {
            assertNotNull(in, "fixture offers-in-store-response.json missing from test resources");
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Test
    void parsesUserCollectedInStorePrices() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json").setBody(fixture()));
        ApiResponse<List<ProductWithOffers>> response = client.getCurrentOffers("011110838360");
        assertEquals("/v1/products/offers", server.takeRequest().getRequestUrl().encodedPath());
        ProductWithOffers product = response.getData().get(0);
        InStorePrice inStore = product.getInStorePrices().get(0);
        assertEquals("user-collected", inStore.getSource());
        assertEquals("Kroger", inStore.getRetailer());
        assertEquals("1014 Vine St, Cincinnati, OH 45202, USA", inStore.getStore().getAddress());
        assertEquals(39.1086, inStore.getStore().getLatitude());
        assertEquals(3.49, inStore.getPrice());
        assertEquals("2026-11-07", inStore.getObservedOn());
        assertEquals("Walmart", product.getOffers().get(0).getRetailer());
    }

    @Test
    void emptyWhenTheServerSendsNone() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json")
            .setBody(fixture().replaceAll("(?s),\\s*\"in_store_prices\": \\[.*?\\]\\s*\\}\\s*\\]", "}]")));
        ProductWithOffers product = client.getCurrentOffers("011110838360").getData().get(0);
        assertNotNull(product.getInStorePrices());
        assertTrue(product.getInStorePrices().isEmpty());
    }
}
