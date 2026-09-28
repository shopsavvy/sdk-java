package com.shopsavvy.sdk.models;

import java.util.ArrayList;

/**
 * Offer returned by {@code getPriceHistory()}: every field of {@link Offer} (id, retailer,
 * price, currency, availability, condition, URL, seller, timestamp) plus its {@code history}
 * array of {@link PriceHistoryEntry} points, newest first.
 *
 * <p>This used to bind the list to a {@code price_history} JSON property. The API has never
 * sent a key by that name — history has always arrived under {@code history} — so
 * {@code getPriceHistory()} returned null for every caller, on every successful response
 * (ShopSavvy prospector-audit s28-t2-2).
 *
 * <p>It now extends {@link Offer} rather than re-declaring the same nine fields, so the two
 * cannot drift apart. {@code history} defaults to an empty list: an offer with no archived
 * points (every eBay listing, for one) arrives as {@code "history": []}, never null.
 */
public class OfferWithHistory extends Offer {
    public OfferWithHistory() {
        setHistory(new ArrayList<PriceHistoryEntry>());
    }
}
