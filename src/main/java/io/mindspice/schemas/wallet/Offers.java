package io.mindspice.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;


public record Offers(

	@JsonProperty("offers") List<String> offers,
	@JsonProperty("success") boolean success,
	@JsonProperty("error") String error,
	@JsonProperty("trade_records") List<TradeRecord> tradeRecords
) {
	public Offers {
		offers = offers != null ? Collections.unmodifiableList(offers) : List.of();
		tradeRecords = tradeRecords != null ? Collections.unmodifiableList(tradeRecords) : List.of();
	}
}