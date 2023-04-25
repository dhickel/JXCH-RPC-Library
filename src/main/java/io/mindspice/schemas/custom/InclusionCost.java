package io.mindspice.schemas.custom;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InclusionCost(

	@JsonProperty("fee_to_spend")
	String feeToSpend,

	@JsonProperty("cost")
	String cost,

	@JsonProperty("min_valid_fee")
	String minValidFee,

	@JsonProperty("room_in_mempool")
	String roomInMempool
) {
}