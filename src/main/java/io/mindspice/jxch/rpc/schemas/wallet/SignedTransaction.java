package io.mindspice.jxch.rpc.schemas.wallet;

import java.util.List;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.schemas.object.Coin;
import io.mindspice.jxch.rpc.schemas.object.SpendBundle;


public record SignedTransaction(
        @JsonProperty("amount") BigInteger amount,
        @JsonProperty("spend_bundle") SpendBundle spendBundle,
        @JsonProperty("additions") List<Coin> additions,
        @JsonProperty("created_at_time") BigInteger createdAtTime,
        @JsonProperty("to_puzzle_hash") String toPuzzleHash,
        @JsonProperty("sent_to") List<List<Object>> sentTo,
        @JsonProperty("removals") List<Coin> removals,
        @JsonProperty("confirmed_at_height") long confirmedAtHeight,
        @JsonProperty("type") long type,
        @JsonProperty("confirmed") boolean confirmed,
        @JsonProperty("sent") long sent,
        @JsonProperty("fee_amount") BigInteger feeAmount,
        @JsonProperty("wallet_id") long walletId,
        @JsonProperty("trade_id") String tradeId,
        @JsonProperty("memos") JsonNode memos,
        @JsonProperty("name") String name,
        @JsonProperty("to_address") String toAddress,
        @JsonProperty("valid_times") ValidTimes validTimes
) {
    public SignedTransaction(long amount, SpendBundle spendBundle, List<Coin> additions, long createdAtTime,
            String toPuzzleHash, List<List<Object>> sentTo, List<Coin> removals, int confirmedAtHeight,
            int type, boolean confirmed, long sent, long feeAmount, int walletId, String tradeId,
            JsonNode memos, String name) {
        this(BigInteger.valueOf(amount), spendBundle, additions, BigInteger.valueOf(createdAtTime), toPuzzleHash, sentTo, removals,
                confirmedAtHeight, type, confirmed, sent, BigInteger.valueOf(feeAmount), walletId, tradeId, memos, name, null, null);
    }
}