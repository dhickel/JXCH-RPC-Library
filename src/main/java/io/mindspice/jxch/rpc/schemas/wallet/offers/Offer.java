package io.mindspice.jxch.rpc.schemas.wallet.offers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.schemas.wallet.Transaction;
import java.util.List;


public record Offer(

	@JsonProperty("offer") String offer,
	@JsonProperty("success") boolean success,
	@JsonProperty("error") String error,
	@JsonProperty("trade_record") TradeRecord tradeRecord1,
	@JsonProperty("transactions") List<Transaction> transactions,
	@JsonProperty("unsigned_transactions") List<JsonNode> unsignedTransactions
) {
	public Offer {
		transactions = transactions != null ? List.copyOf(transactions) : List.of();
		unsignedTransactions = unsignedTransactions != null ? List.copyOf(unsignedTransactions) : List.of();
	}

	public Offer(String offer, boolean success, String error, TradeRecord tradeRecord1) {
		this(offer, success, error, tradeRecord1, List.of(), List.of());
	}

	public TradeRecord tradeRecord() {
		return tradeRecord1;
	}
}