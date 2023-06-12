package io.mindspice.schemas.wallet.offers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.schemas.object.Coin;


public record TradeRecord(
	@JsonProperty("summary") OfferSummary summary,
	@JsonProperty("coins_of_interest") List<Coin> coinsOfInterest,
	@JsonProperty("accepted_at_time") int acceptedAtTime,
	@JsonProperty("trade_id") String tradeId,
	@JsonProperty("is_my_offer") boolean isMyOffer,
	@JsonProperty("created_at_time") int createdAtTime,
	@JsonProperty("pending") Map<String, Long> pending,
	@JsonProperty("sent_to") List<String> sentTo,
	@JsonProperty("confirmed_at_index") int confirmedAtIndex,
	@JsonProperty("sent") int sent,
	@JsonProperty("status") String status,
	@JsonProperty("taken_offer") String takenOffer
) {
	public TradeRecord {
		coinsOfInterest = coinsOfInterest != null ? Collections.unmodifiableList(coinsOfInterest) : List.of();
		pending = pending != null ? Collections.unmodifiableMap(pending) : Map.of();
		sentTo = sentTo != null ? Collections.unmodifiableList(sentTo) : List.of();
	}
}