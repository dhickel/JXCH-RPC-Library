package io.mindspice.jxch.rpc.schemas.wallet.offers;

import com.fasterxml.jackson.annotation.JsonProperty;


//FIXME this need to handle muultiple trade records
public record Offer(

	@JsonProperty("offer") String offer,
	@JsonProperty("success") boolean success,
	@JsonProperty("error") String error,
	@JsonProperty("trade_record") TradeRecord tradeRecord1
) {
}