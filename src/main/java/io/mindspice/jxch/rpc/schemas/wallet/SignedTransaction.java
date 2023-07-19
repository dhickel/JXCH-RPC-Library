package io.mindspice.jxch.rpc.schemas.wallet;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.schemas.object.Coin;
import io.mindspice.jxch.rpc.schemas.object.SpendBundle;


public record SignedTransaction(
        @JsonProperty("amount") long amount,
        @JsonProperty("spend_bundle") SpendBundle spendBundle,
        @JsonProperty("additions") List<Coin> additions,
        @JsonProperty("created_at_time") int createdAtTime,
        @JsonProperty("to_puzzle_hash") String toPuzzleHash,
        @JsonProperty("sent_to") List<String> sentTo,
        @JsonProperty("removals") List<Coin> removals,
        @JsonProperty("confirmed_at_height") int confirmedAtHeight,
        @JsonProperty("type") int type,
        @JsonProperty("confirmed") boolean confirmed,
        @JsonProperty("sent") long sent,
        @JsonProperty("fee_amount") long feeAmount,
        @JsonProperty("wallet_id") int walletId,
        @JsonProperty("trade_id") String tradeId,
        @JsonProperty("memos") JsonNode memos,
        @JsonProperty("name") String name
) {
}