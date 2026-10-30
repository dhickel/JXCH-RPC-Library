package io.mindspice.jxch.rpc.schemas.wallet.offers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.math.BigInteger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.jxch.rpc.schemas.object.Coin;
import io.mindspice.jxch.rpc.schemas.wallet.ValidTimes;


public record TradeRecord(
	@JsonProperty("summary") OfferSummary summary,
	@JsonProperty("coins_of_interest") List<Coin> coinsOfInterest,
	@JsonProperty("accepted_at_time") BigInteger acceptedAtTime,
	@JsonProperty("trade_id") String tradeId,
	@JsonProperty("is_my_offer") boolean isMyOffer,
	@JsonProperty("created_at_time") BigInteger createdAtTime,
	@JsonProperty("pending") Map<String, BigInteger> pending,
	@JsonProperty("sent_to") List<List<Object>> sentTo,
	@JsonProperty("confirmed_at_index") long confirmedAtIndex,
	@JsonProperty("sent") long sent,
	@JsonProperty("status") String status,
	@JsonProperty("taken_offer") String takenOffer,
	@JsonProperty("valid_times") ValidTimes validTimes
) {
	public TradeRecord(OfferSummary summary, List<Coin> coinsOfInterest, int acceptedAtTime,
			String tradeId, boolean isMyOffer, long createdAtTime, Map<String, Long> pending,
			List<List<Object>> sentTo, int confirmedAtIndex, int sent, String status, String takenOffer) {
		this(summary, coinsOfInterest, Long.valueOf(acceptedAtTime), tradeId, isMyOffer, createdAtTime,
				pending, sentTo, confirmedAtIndex, sent, status, takenOffer);
	}

	public TradeRecord(OfferSummary summary, List<Coin> coinsOfInterest, Long acceptedAtTime,
			String tradeId, boolean isMyOffer, long createdAtTime, Map<String, Long> pending,
			List<List<Object>> sentTo, int confirmedAtIndex, int sent, String status, String takenOffer) {
		this(summary, coinsOfInterest, acceptedAtTime == null ? null : BigInteger.valueOf(acceptedAtTime),
				tradeId, isMyOffer, BigInteger.valueOf(createdAtTime),
				pending == null ? Map.of() : pending.entrySet().stream()
						.collect(Collectors.toMap(Map.Entry::getKey, e -> BigInteger.valueOf(e.getValue()))),
				sentTo, confirmedAtIndex, sent, status, takenOffer, null);
	}
	public TradeRecord {
		coinsOfInterest = coinsOfInterest != null ? Collections.unmodifiableList(coinsOfInterest) : List.of();
		pending = pending != null ? Collections.unmodifiableMap(pending) : Map.of();
		sentTo = sentTo != null ? Collections.unmodifiableList(sentTo) : List.of();
	}
}