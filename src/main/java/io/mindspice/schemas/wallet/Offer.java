package io.mindspice.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Offer(

	@JsonProperty("offer") String offer,
	@JsonProperty("success") boolean success,
	@JsonProperty("error") String error,
	@JsonProperty("trade_record") TradeRecord tradeRecord
) {
}