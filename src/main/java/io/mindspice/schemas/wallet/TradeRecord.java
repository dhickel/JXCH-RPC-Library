package io.mindspice.schemas.wallet;

import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.schemas.object.Coin;


public record TradeRecord(

	@JsonProperty("offer") String offer,
	@JsonProperty("coins_of_interest") List<Coin> coinsOfInterest,
	@JsonProperty("accepted_at_time") Object acceptedAtTime,
	@JsonProperty("trade_id") String tradeId,
	@JsonProperty("is_my_offer") boolean isMyOffer,
	@JsonProperty("created_at_time") int createdAtTime,
	@JsonProperty("sent_to") List<String> sentTo,
	@JsonProperty("confirmed_at_height") int confirmedAtHeight,
	@JsonProperty("sent") long sent,
	@JsonProperty("taken_offer") Object takenOffer,
	@JsonProperty("status") int status
) {
	public TradeRecord {
		coinsOfInterest = coinsOfInterest != null ? Collections.unmodifiableList(coinsOfInterest) : List.of();
		sentTo = sentTo != null ? Collections.unmodifiableList(sentTo) : List.of();
	}
}