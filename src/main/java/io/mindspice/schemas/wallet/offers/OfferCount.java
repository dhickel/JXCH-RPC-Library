package io.mindspice.schemas.wallet.offers;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OfferCount(
	@JsonProperty("total") int total,
	@JsonProperty("taken_offers_count") int takenOffersCount,
	@JsonProperty("my_offers_count") int myOffersCount,
	@JsonProperty("success") boolean success,
	@JsonProperty("error")
	String error
) {
}