package io.mindspice.jxch.rpc.schemas.wallet;

import java.util.Collections;
import java.util.List;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.schemas.object.Coin;
import io.mindspice.jxch.rpc.schemas.object.SpendBundle;


public record Transaction(
        @JsonProperty("confirmed_at_height") long confirmedAtHeight,
        @JsonProperty("created_at_time") BigInteger createdAtTime,
        @JsonProperty("to_puzzle_hash") String toPuzzleHash,
        @JsonProperty("amount") BigInteger amount,
        @JsonProperty("fee_amount") BigInteger feeAmount,
        @JsonProperty("confirmed") boolean confirmed,
        @JsonProperty("sent") long sent,
        @JsonProperty("spend_bundle") SpendBundle spendBundle,
        @JsonProperty("additions") List<Coin> additions,
        @JsonProperty("removals") List<Coin> removals,
        @JsonProperty("wallet_id") long walletId,
        @JsonProperty("sent_to") List<List<Object>> sentTo,
        @JsonAlias({"to_address", "send_to"})
        @JsonProperty("to_address") String toAddress,
        @JsonProperty("trade_id") String tradeId,
        @JsonProperty("type") long type,
        @JsonProperty("name") String name,
        @JsonProperty("memos") JsonNode memos,
        @JsonProperty("transaction_id") String transactionId,
        @JsonProperty("valid_times") ValidTimes validTimes,
        @JsonProperty("metadata") JsonNode metadata
) {
    public Transaction(int confirmedAtHeight, long createdAtTime, String toPuzzleHash, long amount,
            long feeAmount, boolean confirmed, long sent, SpendBundle spendBundle, List<Coin> additions,
            List<Coin> removals, int walletId, List<? extends List<?>> sentTo, String toAddress,
            String tradeId, int type, String name, JsonNode memos, String transactionId) {
        this(confirmedAtHeight, BigInteger.valueOf(createdAtTime), toPuzzleHash, BigInteger.valueOf(amount), BigInteger.valueOf(feeAmount), confirmed, sent,
                spendBundle, additions, removals, walletId,
                sentTo == null ? List.of() : sentTo.stream().map(row -> new java.util.ArrayList<Object>(row))
                        .map(row -> (List<Object>) row).toList(),
                toAddress, tradeId, type, name, memos, transactionId, null, null);
    }

    public Transaction {
        additions = additions != null ? Collections.unmodifiableList(additions) : List.of();
        sentTo = sentTo != null ? Collections.unmodifiableList(sentTo) : List.of();
        removals = removals != null ? Collections.unmodifiableList(removals) : List.of();
    }
}