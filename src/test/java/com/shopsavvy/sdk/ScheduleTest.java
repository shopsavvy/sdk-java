package com.shopsavvy.sdk;

import com.shopsavvy.sdk.models.ApiResponse;
import com.shopsavvy.sdk.models.ScheduledProduct;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The schedule endpoints read ONLY query parameters (PUT/DELETE /products/scheduled with
 * ids, schedule, retailer). These run the real client against a local server and assert the
 * method, path, query string and absence of a body, and parse responses of the shape the API
 * actually sends.
 */
class ScheduleTest {
    private static final String SCHEDULED_TWO =
        "{\"success\":true,\"data\":["
        + "{\"title\":\"Keurig K-Mini\",\"shopsavvy\":\"3ONn300xybP3y66ibqc1\",\"barcode\":\"611247373064\",\"amazon\":\"B07G14HTBZ\",\"brand\":\"Keurig\",\"images\":[],\"schedule\":\"daily\",\"retailer\":\"amazon.com\"},"
        + "{\"title\":\"Keurig K-Elite\",\"shopsavvy\":\"DrKWneG0MpFlZpwZXNYa\",\"barcode\":\"611247369449\",\"amazon\":null,\"brand\":\"Keurig\",\"images\":[],\"schedule\":\"daily\",\"retailer\":\"amazon.com\"}"
        + "],\"meta\":{\"request_id\":\"r1\",\"credits_used\":2,\"credits_remaining\":998,\"rate_limit_remaining\":999}}";

    private static final String SCHEDULED_ONE_ALL_RETAILERS =
        "{\"success\":true,\"data\":["
        + "{\"title\":\"Keurig K-Mini\",\"shopsavvy\":\"3ONn300xybP3y66ibqc1\",\"barcode\":\"611247373064\",\"schedule\":\"hourly\"}"
        + "],\"meta\":{\"request_id\":\"r2\",\"credits_used\":1,\"credits_remaining\":997,\"rate_limit_remaining\":998}}";

    private static final String UNSCHEDULED =
        "{\"success\":true,\"message\":\"Products successfully removed from schedule\","
        + "\"meta\":{\"request_id\":\"r3\",\"credits_used\":0,\"credits_remaining\":0,\"rate_limit_remaining\":0}}";

    // A PUT response item carrying every field publicProductForProduct can emit.
    private static final String SCHEDULED_FULL_PRODUCT =
        "{\"success\":true,\"data\":[{"
        + "\"title\":\"Keurig K-Mini Single Serve Coffee Maker\",\"category\":\"Coffee Makers\",\"brand\":\"Keurig\","
        + "\"color\":\"Black\",\"shopsavvy\":\"3ONn300xybP3y66ibqc1\",\"barcode\":\"611247373064\",\"amazon\":\"B07G14HTBZ\","
        + "\"model\":\"K-Mini\",\"mpn\":\"5000200237\",\"images\":[\"https://x.shopsavvy.com/a.jpg\",\"https://x.shopsavvy.com/b.jpg\"],"
        + "\"title_short\":\"Keurig K-Mini\",\"slug\":\"keurig-k-mini\",\"description\":\"Fits anywhere.\","
        + "\"categories\":[\"Kitchen\",\"Coffee Makers\"],\"attributes\":{\"Capacity\":\"12 oz\"},"
        + "\"rating\":{\"value\":4.5,\"count\":1200},\"score\":{\"overall\":0.82},\"keywords\":[\"coffee\"],"
        + "\"identifiers\":{\"amazon\":\"B07G14HTBZ\",\"upc\":\"611247373064\"},"
        + "\"schedule\":\"weekly\",\"retailer\":\"bestbuy.com\"}],"
        + "\"meta\":{\"request_id\":\"req_abc\",\"credits_used\":1,\"credits_remaining\":49,\"rate_limit_remaining\":59}}";

    // GET list: an interval with no Data API label (e.g. 4h from ShopSavvy Business) omits
    // `schedule`, a product scheduled at every retailer omits `retailer`, and the list meta is all zeros.
    private static final String LIST_WITH_UNLABELED_INTERVAL =
        "{\"success\":true,\"data\":["
        + "{\"title\":\"Keurig K-Mini\",\"shopsavvy\":\"3ONn300xybP3y66ibqc1\",\"barcode\":\"611247373064\",\"amazon\":\"B07G14HTBZ\",\"images\":[]},"
        + "{\"title\":\"Keurig K-Elite\",\"shopsavvy\":\"DrKWneG0MpFlZpwZXNYa\",\"barcode\":\"611247369449\",\"images\":[],\"schedule\":\"hourly\",\"retailer\":\"target.com\"}"
        + "],\"meta\":{\"request_id\":\"req_list\",\"credits_used\":0,\"credits_remaining\":0,\"rate_limit_remaining\":0}}";

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

    private void enqueue(String body) {
        server.enqueue(new MockResponse().setResponseCode(200).setHeader("Content-Type", "application/json").setBody(body));
    }

    private static void assertNoBody(RecordedRequest request) {
        assertEquals(0, request.getBodySize(), "schedule endpoints read only the query string; no body should be sent");
    }

    @Test
    void singleScheduleWithoutRetailerSendsPutWithQueryParams() throws Exception {
        enqueue(SCHEDULED_ONE_ALL_RETAILERS);

        ApiResponse<List<ScheduledProduct>> response = client.scheduleProductMonitoring("611247373064", "hourly");

        RecordedRequest request = server.takeRequest();
        assertEquals("PUT", request.getMethod());
        HttpUrl url = request.getRequestUrl();
        assertEquals("/v1/products/scheduled", url.encodedPath());
        assertEquals("ids=611247373064&schedule=hourly", url.encodedQuery());
        assertNull(url.queryParameter("retailer"));
        assertNoBody(request);

        assertTrue(response.getSuccess());
        assertEquals(1, response.getData().size());
        ScheduledProduct product = response.getData().get(0);
        assertEquals("3ONn300xybP3y66ibqc1", product.getShopsavvy());
        assertEquals("611247373064", product.getBarcode());
        assertEquals("hourly", product.getSchedule());
        assertNull(product.getRetailer());
        assertEquals(1, response.getMeta().getCreditsUsed().intValue());
    }

    @Test
    void singleScheduleWithRetailerSendsRetailerQueryParam() throws Exception {
        enqueue(SCHEDULED_TWO);

        client.scheduleProductMonitoring("611247373064", "daily", "amazon.com");

        RecordedRequest request = server.takeRequest();
        assertEquals("PUT", request.getMethod());
        HttpUrl url = request.getRequestUrl();
        assertEquals("/v1/products/scheduled", url.encodedPath());
        assertEquals("ids=611247373064&schedule=daily&retailer=amazon.com", url.encodedQuery());
        assertNoBody(request);
    }

    @Test
    void batchScheduleJoinsIdsWithEncodedCommas() throws Exception {
        enqueue(SCHEDULED_TWO);

        ApiResponse<List<ScheduledProduct>> response = client.scheduleProductsMonitoring(
            Arrays.asList("611247373064", "611247369449"), "daily", "amazon.com");

        RecordedRequest request = server.takeRequest();
        assertEquals("PUT", request.getMethod());
        HttpUrl url = request.getRequestUrl();
        assertEquals("/v1/products/scheduled", url.encodedPath());
        assertEquals("ids=611247373064%2C611247369449&schedule=daily&retailer=amazon.com", url.encodedQuery());
        assertEquals("611247373064,611247369449", url.queryParameter("ids"));
        assertNoBody(request);

        assertEquals(2, response.getData().size());
        assertEquals("DrKWneG0MpFlZpwZXNYa", response.getData().get(1).getShopsavvy());
        assertNull(response.getData().get(1).getAmazon());
        assertEquals("daily", response.getData().get(1).getSchedule());
        assertEquals("amazon.com", response.getData().get(1).getRetailer());
    }

    @Test
    void batchScheduleWithoutRetailerOmitsRetailer() throws Exception {
        enqueue(SCHEDULED_TWO);

        client.scheduleProductsMonitoring(Arrays.asList("611247373064", "B07G14HTBZ"), "weekly");

        RecordedRequest request = server.takeRequest();
        assertEquals("PUT", request.getMethod());
        HttpUrl url = request.getRequestUrl();
        assertEquals("/v1/products/scheduled", url.encodedPath());
        assertEquals("ids=611247373064%2CB07G14HTBZ&schedule=weekly", url.encodedQuery());
        assertNoBody(request);
    }

    @Test
    void singleUnscheduleSendsDeleteWithIdsQueryParam() throws Exception {
        enqueue(UNSCHEDULED);

        ApiResponse<Void> response = client.removeProductFromSchedule("611247373064");

        RecordedRequest request = server.takeRequest();
        assertEquals("DELETE", request.getMethod());
        HttpUrl url = request.getRequestUrl();
        assertEquals("/v1/products/scheduled", url.encodedPath());
        assertEquals("ids=611247373064", url.encodedQuery());
        assertNoBody(request);

        assertTrue(response.getSuccess());
        assertEquals("Products successfully removed from schedule", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void batchUnscheduleJoinsIds() throws Exception {
        enqueue(UNSCHEDULED);

        client.removeProductsFromSchedule(Arrays.asList("611247373064", "611247369449"));

        RecordedRequest request = server.takeRequest();
        assertEquals("DELETE", request.getMethod());
        HttpUrl url = request.getRequestUrl();
        assertEquals("/v1/products/scheduled", url.encodedPath());
        assertEquals("ids=611247373064%2C611247369449", url.encodedQuery());
        assertNoBody(request);
    }

    @Test
    void listScheduledParsesProductsWithSchedule() throws Exception {
        enqueue(SCHEDULED_TWO);

        ApiResponse<List<ScheduledProduct>> response = client.getScheduledProducts();

        RecordedRequest request = server.takeRequest();
        assertEquals("GET", request.getMethod());
        assertEquals("/v1/products/scheduled", request.getRequestUrl().encodedPath());
        assertNull(request.getRequestUrl().encodedQuery());

        assertEquals(2, response.getData().size());
        ScheduledProduct first = response.getData().get(0);
        assertEquals("Keurig K-Mini", first.getTitle());
        assertEquals("daily", first.getSchedule());
        assertEquals("amazon.com", first.getRetailer());
    }

    @Test
    void scheduleParsesEveryPublicProductFieldPlusScheduleRetailerAndMeta() throws Exception {
        enqueue(SCHEDULED_FULL_PRODUCT);

        ApiResponse<List<ScheduledProduct>> response = client.scheduleProductMonitoring("611247373064", "weekly", "bestbuy.com");

        assertTrue(response.getSuccess());
        assertNull(response.getMessage());
        ScheduledProduct p = response.getData().get(0);
        assertEquals("Keurig K-Mini Single Serve Coffee Maker", p.getTitle());
        assertEquals("Coffee Makers", p.getCategory());
        assertEquals("Keurig", p.getBrand());
        assertEquals("Black", p.getColor());
        assertEquals("3ONn300xybP3y66ibqc1", p.getShopsavvy());
        assertEquals("611247373064", p.getBarcode());
        assertEquals("B07G14HTBZ", p.getAmazon());
        assertEquals("K-Mini", p.getModel());
        assertEquals("5000200237", p.getMpn());
        assertEquals(Arrays.asList("https://x.shopsavvy.com/a.jpg", "https://x.shopsavvy.com/b.jpg"), p.getImages());
        assertEquals("Keurig K-Mini", p.getTitleShort());
        assertEquals("keurig-k-mini", p.getSlug());
        assertEquals("Fits anywhere.", p.getDescription());
        assertEquals(Arrays.asList("Kitchen", "Coffee Makers"), p.getCategories());
        assertEquals("12 oz", p.getAttributes().get("Capacity"));
        assertEquals(4.5, ((Number) p.getRating().get("value")).doubleValue());
        assertEquals(1200, ((Number) p.getRating().get("count")).intValue());
        assertEquals(0.82, ((Number) p.getScore().get("overall")).doubleValue());
        assertEquals(Arrays.asList("coffee"), p.getKeywords());
        assertEquals("611247373064", p.getIdentifiers().get("upc"));
        assertEquals("weekly", p.getSchedule());
        assertEquals("weekly", p.getFrequency());
        assertEquals("bestbuy.com", p.getRetailer());

        assertEquals("req_abc", response.getMeta().getRequestId());
        assertEquals(1, response.getMeta().getCreditsUsed().intValue());
        assertEquals(49, response.getMeta().getCreditsRemaining().intValue());
        assertEquals(59, response.getMeta().getRateLimitRemaining().intValue());
    }

    @Test
    void unscheduleParsesSuccessMessageAndZeroMetaWithNoData() throws Exception {
        enqueue(UNSCHEDULED);

        ApiResponse<Void> response = client.removeProductsFromSchedule(Arrays.asList("611247373064", "611247369449"));

        assertTrue(response.getSuccess());
        assertEquals("Products successfully removed from schedule", response.getMessage());
        assertNull(response.getData());
        assertEquals("r3", response.getMeta().getRequestId());
        assertEquals(0, response.getMeta().getCreditsUsed().intValue());
        assertEquals(0, response.getMeta().getCreditsRemaining().intValue());
        assertEquals(0, response.getMeta().getRateLimitRemaining().intValue());
    }

    @Test
    void listScheduledLeavesScheduleAndRetailerNullWhenOmitted() throws Exception {
        enqueue(LIST_WITH_UNLABELED_INTERVAL);

        ApiResponse<List<ScheduledProduct>> response = client.getScheduledProducts();

        assertTrue(response.getSuccess());
        assertEquals(2, response.getData().size());
        ScheduledProduct unlabeled = response.getData().get(0);
        assertEquals("3ONn300xybP3y66ibqc1", unlabeled.getShopsavvy());
        assertEquals("B07G14HTBZ", unlabeled.getAmazon());
        assertNull(unlabeled.getSchedule());
        assertNull(unlabeled.getRetailer());
        assertNull(unlabeled.getBrand());
        ScheduledProduct second = response.getData().get(1);
        assertEquals("hourly", second.getSchedule());
        assertEquals("target.com", second.getRetailer());
        assertNull(second.getAmazon());
        assertEquals("req_list", response.getMeta().getRequestId());
        assertEquals(0, response.getMeta().getCreditsUsed().intValue());
    }
}
