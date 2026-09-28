package com.shopsavvy.sdk.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

/**
 * One product returned by {@code getPriceHistory()}.
 *
 * <p>{@code GET /products/offers/history} returns one entry PER PRODUCT — the same product
 * fields as {@code GET /products} (title, barcode, amazon, brand, images, …) — each carrying
 * an {@code offers} array, and each offer carrying its own {@code history} array:
 *
 * <pre>
 * { "success": true,
 *   "data": [ { ...product fields,
 *               "offers": [ { ...offer fields, "history": [ {timestamp, price, currency, availability} ] } ] } ],
 *   "meta": { ... } }
 * </pre>
 *
 * <p>Until 1.4.0 the SDK typed {@code data} as a flat {@code List<OfferWithHistory>}, i.e. it
 * read each PRODUCT as if it were an offer. Because the client's ObjectMapper ignores unknown
 * properties, that did not throw — it silently produced offers whose id, price, retailer and
 * history were all null, for every caller, on every successful response.
 */
public class ProductWithOfferHistory extends ProductDetails {
    @JsonProperty("offers")
    private List<OfferWithHistory> offers = new ArrayList<OfferWithHistory>();

    public ProductWithOfferHistory() {}

    public List<OfferWithHistory> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferWithHistory> offers) {
        this.offers = offers;
    }
}
