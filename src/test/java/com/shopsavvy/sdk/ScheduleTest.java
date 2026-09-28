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
}
