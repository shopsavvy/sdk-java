package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A product scheduled for regular refresh, as returned by {@code scheduleProductMonitoring()},
 * {@code scheduleProductsMonitoring()} and {@code getScheduledProducts()}.
 *
 * <p>The API returns the same product fields as {@code GET /products} (title, shopsavvy,
 * barcode, amazon, brand, images, …) plus {@code schedule} ({@code "hourly"}, {@code "daily"}
 * or {@code "weekly"}) and, when the schedule is limited to one retailer, {@code retailer}.
 *
 * <p>This model used to declare {@code product_id}, {@code identifier}, {@code frequency},
 * {@code created_at} and {@code last_refreshed} — keys the API has never sent — so every one of
 * them read null. {@code getFrequency()} is kept as a deprecated alias for {@code getSchedule()};
 * {@code getProductId()} (inherited) returns the ShopSavvy product id.
 */
public class ScheduledProduct extends ProductDetails {
    /**
     * Refresh interval: "hourly", "daily" or "weekly". Null for a product scheduled at an
     * interval the Data API has no label for (e.g. one set up through ShopSavvy Business).
     */
    @JsonProperty("schedule")
    private String schedule;

    /** Retailer domain the schedule is limited to; null when all retailers are refreshed. */
    @JsonProperty("retailer")
    private String retailer;

    public ScheduledProduct() {}

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    /** @deprecated Use getSchedule() instead */
    @Deprecated
    public String getFrequency() {
        return schedule;
    }
}
