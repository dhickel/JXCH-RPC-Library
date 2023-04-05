package io.mindspice.schemas.wallet.offers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;


public record Offers(

	@JsonProperty("offers") List<String> offers,
	@JsonProperty("success") boolean success,
	@JsonProperty("error") String error,
	@JsonProperty("trade_records") List<TradeRecord> tradeRecord1s
) {
	public Offers {
		offers = offers != null ? Collections.unmodifiableList(offers) : List.of();
		tradeRecord1s = tradeRecord1s != null ? Collections.unmodifiableList(tradeRecord1s) : List.of();
	}
}