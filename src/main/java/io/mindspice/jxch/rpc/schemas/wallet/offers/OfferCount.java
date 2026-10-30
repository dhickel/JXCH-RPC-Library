package io.mindspice.jxch.rpc.schemas.wallet.offers;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OfferCount(
	@JsonProperty("total") long total,
	@JsonProperty("taken_offers_count") long takenOffersCount,
	@JsonProperty("my_offers_count") long myOffersCount,
	@JsonProperty("success") boolean success,
	@JsonProperty("error")
	String error
) {
}