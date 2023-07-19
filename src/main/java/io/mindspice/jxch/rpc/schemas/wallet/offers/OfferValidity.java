package io.mindspice.jxch.rpc.schemas.wallet.offers;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OfferValidity(

	@JsonProperty("valid") boolean valid,
	@JsonProperty("success") boolean success,
	@JsonProperty("id") String id,
	@JsonProperty("error") String error
) {
}