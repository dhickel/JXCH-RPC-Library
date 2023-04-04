package io.mindspice.schemas.wallet;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.schemas.object.Coin;
import io.mindspice.schemas.object.SpendBundle;


public record Transaction(
        @JsonProperty("amount") int amount,
        @JsonProperty("spend_bundle") SpendBundle spendBundle,
        @JsonProperty("additions") List<Coin> additions,
        @JsonProperty("created_at_time") int createdAtTime,
        @JsonProperty("to_puzzle_hash") String toPuzzleHash,
        @JsonProperty("sent_to") List<String> sentTo,
        @JsonProperty("removals") List<Coin> removals,
        @JsonProperty("to_address") String toAddress,
        @JsonProperty("confirmed_at_height") int confirmedAtHeight,
        @JsonProperty("type") int type,
        @JsonProperty("confirmed") boolean confirmed,
        @JsonProperty("sent") long sent,
        @JsonProperty("fee_amount") long feeAmount,
        @JsonProperty("wallet_id") int walletId,
        @JsonProperty("trade_id") Object tradeId,
        @JsonProperty("memos") Map<String, String> memos,
        @JsonProperty("name") String name
) {
    public Transaction {
        additions = additions != null ? Collections.unmodifiableList(additions) : List.of();
        sentTo = sentTo != null ? Collections.unmodifiableList(sentTo) : List.of();
        removals = removals != null ? Collections.unmodifiableList(removals) : List.of();
        memos = memos !=null ? Collections.unmodifiableMap(memos) : Map.of();
    }
}