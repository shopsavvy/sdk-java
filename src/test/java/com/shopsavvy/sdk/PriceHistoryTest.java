package com.shopsavvy.sdk;

import com.shopsavvy.sdk.models.ApiResponse;
import com.shopsavvy.sdk.models.OfferWithHistory;
import com.shopsavvy.sdk.models.PriceHistoryEntry;
import com.shopsavvy.sdk.models.ProductWithOfferHistory;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Feeds a response of the exact shape GET /products/offers/history returns (one entry per
 * product, each with offers, each offer with history points) through the real client —
 * real OkHttp request, real ObjectMapper parsing — and asserts the parsed values.
 */
class PriceHistoryTest {
    private MockWebServer server;
    private ShopSavvyClient client;

    @BeforeEach
    void setUp() throws IOException {
        server = new MockWebServer();
        server.start();
        String baseUrl = server.url("/v1").toString();
        client = new ShopSavvyClient("ss_test_fixturekey123", baseUrl, 5);
    }

    @AfterEach
    void tearDown() throws IOException {
        client.close();
        server.shutdown();
    }

    private static String fixture() throws IOException {
        try (InputStream in = PriceHistoryTest.class.getResourceAsStream("/price-history-response.json")) {
            assertNotNull(in, "fixture price-history-response.json missing from test resources");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int n;
            while ((n = in.read(buf)) != -1) {
                out.write(buf, 0, n);
            }
            return new String(out.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    @Test
    void sendsStartAndEndAndRetailerOnTheWire() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json").setBody(fixture()));

        client.getPriceHistory("611247373064", "2022-11-20", "2022-11-27", "amazon.com", null);

        RecordedRequest request = server.takeRequest();
        assertEquals("GET", request.getMethod());
        HttpUrl url = request.getRequestUrl();
        assertNotNull(url);
        assertEquals("/v1/products/offers/history", url.encodedPath());
        assertEquals("611247373064", url.queryParameter("ids"));
        assertEquals("2022-11-20", url.queryParameter("start"));
        assertEquals("2022-11-27", url.queryParameter("end"));
        assertEquals("amazon.com", url.queryParameter("retailer"));
        assertNull(url.queryParameter("start_date"));
        assertNull(url.queryParameter("end_date"));
        assertNull(url.queryParameter("format"));
        assertEquals("Bearer ss_test_fixturekey123", request.getHeader("Authorization"));
        assertEquals("ShopSavvy-Java-SDK/" + ShopSavvyClient.VERSION, request.getHeader("User-Agent"));
    }

    @Test
    void parsesProductsOffersAndHistoryPoints() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json").setBody(fixture()));

        ApiResponse<List<ProductWithOfferHistory>> response = client.getPriceHistory("611247373064,611247369449", "2022-11-20", "2022-11-27");

        assertTrue(response.getSuccess());
        assertEquals(14, response.getMeta().getCreditsUsed().intValue());
        assertEquals(986, response.getMeta().getCreditsRemaining().intValue());
        assertEquals(999, response.getMeta().getRateLimitRemaining().intValue());

        List<ProductWithOfferHistory> products = response.getData();
        assertEquals(2, products.size());

        // Product 1: fully populated, two offers
        ProductWithOfferHistory kMini = products.get(0);
        assertEquals("Keurig K-Mini Single Serve Coffee Maker, Black", kMini.getTitle());
        assertEquals("3ONn300xybP3y66ibqc1", kMini.getShopsavvy());
        assertEquals("611247373064", kMini.getBarcode());
        assertEquals("B07G14HTBZ", kMini.getAmazon());
        assertEquals("Keurig", kMini.getBrand());
        assertEquals("K-MINI", kMini.getModel());
        assertEquals("5000200237", kMini.getMpn());
        assertEquals("Keurig K-Mini", kMini.getTitleShort());
        assertEquals(1, kMini.getImages().size());
        assertEquals(51234, ((Number) kMini.getRating().get("count")).intValue());
        assertEquals("611247373064", kMini.getIdentifiers().get("upc"));
        assertEquals(2, kMini.getOffers().size());

        OfferWithHistory amazon = kMini.getOffers().get(0);
        assertEquals("0IUouCFtZEhxeOablTPl", amazon.getId());
        assertEquals("Amazon", amazon.getRetailer());
        assertEquals(74.96, amazon.getPrice(), 0.0001);
        assertEquals("USD", amazon.getCurrency());
        assertEquals("in", amazon.getAvailability());
        assertEquals("new", amazon.getCondition());
        assertEquals("ACME Deals", amazon.getSeller());
        assertEquals("https://www.amazon.com/dp/B07G14HTBZ?m=A1GKQADQC2VI6E", amazon.getUrl());
        assertEquals("2022-11-27T22:36:33.236Z", amazon.getTimestamp());

        List<PriceHistoryEntry> amazonHistory = amazon.getHistory();
        assertEquals(3, amazonHistory.size());
        assertEquals("2022-11-27T22:36:33.236Z", amazonHistory.get(0).getTimestamp());
        assertEquals(74.96, amazonHistory.get(0).getPrice(), 0.0001);
        assertEquals("USD", amazonHistory.get(0).getCurrency());
        assertEquals("in", amazonHistory.get(0).getAvailability());
        assertEquals(70.99, amazonHistory.get(1).getPrice(), 0.0001);
        assertEquals("out", amazonHistory.get(1).getAvailability());
        // Third point: availability omitted (unknown) and currency explicitly null
        assertEquals("2022-11-21T08:15:00.000Z", amazonHistory.get(2).getTimestamp());
        assertEquals(79.99, amazonHistory.get(2).getPrice(), 0.0001);
        assertNull(amazonHistory.get(2).getCurrency());
        assertNull(amazonHistory.get(2).getAvailability());

        OfferWithHistory bestBuy = kMini.getOffers().get(1);
        assertEquals("Z9kQ2mBestBuyOffer01", bestBuy.getId());
        assertEquals("Best Buy", bestBuy.getRetailer());
        assertEquals(59.99, bestBuy.getPrice(), 0.0001);
        assertNull(bestBuy.getAvailability());
        assertNull(bestBuy.getSeller());
        assertEquals(2, bestBuy.getHistory().size());
        assertEquals(64.99, bestBuy.getHistory().get(1).getPrice(), 0.0001);

        // Product 2: sparse product fields, one eBay offer with empty history
        ProductWithOfferHistory kElite = products.get(1);
        assertEquals("DrKWneG0MpFlZpwZXNYa", kElite.getShopsavvy());
        assertEquals("611247369449", kElite.getBarcode());
        assertNull(kElite.getAmazon());
        assertNull(kElite.getCategory());
        assertNull(kElite.getColor());
        assertNull(kElite.getMpn());
        assertTrue(kElite.getImages().isEmpty());
        assertEquals(1, kElite.getOffers().size());
        OfferWithHistory ebay = kElite.getOffers().get(0);
        assertEquals("eBayListing000000001", ebay.getId());
        assertEquals("used", ebay.getCondition());
        assertEquals(89.5, ebay.getPrice(), 0.0001);
        assertNotNull(ebay.getHistory());
        assertTrue(ebay.getHistory().isEmpty());
    }

    @Test
    void offerWithMissingHistoryKeyDefaultsToEmptyList() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json").setBody(
            "{\"success\":true,\"data\":[{\"title\":\"X\",\"shopsavvy\":\"p1\",\"offers\":[{\"id\":\"o1\",\"retailer\":\"Target\",\"price\":1.5}]}]}"));

        ApiResponse<List<ProductWithOfferHistory>> response = client.getPriceHistory("p1", "2022-11-20", "2022-11-27");

        OfferWithHistory offer = response.getData().get(0).getOffers().get(0);
        assertEquals("o1", offer.getId());
        assertNotNull(offer.getHistory());
        assertTrue(offer.getHistory().isEmpty());
        assertNull(response.getMeta());
    }
}
