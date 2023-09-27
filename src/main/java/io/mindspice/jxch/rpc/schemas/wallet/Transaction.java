package io.mindspice.jxch.rpc.schemas.wallet;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.schemas.object.Coin;
import io.mindspice.jxch.rpc.schemas.object.SpendBundle;


public record Transaction(
        @JsonProperty("confirmed_at_height") int confirmedAtHeight,
        @JsonProperty("created_at_time") int createdAtTime,
        @JsonProperty("to_puzzle_hash") String toPuzzleHash,
        @JsonProperty("amount") long amount,
        @JsonProperty("fee_amount") long feeAmount,
        @JsonProperty("confirmed") boolean confirmed,
        @JsonProperty("sent") long sent,
        @JsonProperty("spend_bundle") SpendBundle spendBundle,
        @JsonProperty("additions") List<Coin> additions,
        @JsonProperty("removals") List<Coin> removals,
        @JsonProperty("wallet_id") int walletId,
       // @JsonDeserialize(using = StringListDeserializer.class)
        @JsonProperty("sent_to")
        List<List<String>> sentTo, //Fixme Not sure if this sometimes just returns an unested array? if so flatted with serializer?
        @JsonAlias({"to_address", "send_to"})
        @JsonProperty("to_address") String toAddress,
        @JsonProperty("trade_id") String tradeId,
        @JsonProperty("type") int type,
        @JsonProperty("name") String name,
        @JsonProperty("memos") JsonNode memos,
        @JsonProperty("transaction_id") String transactionId
) {
    public Transaction {
        additions = additions != null ? Collections.unmodifiableList(additions) : List.of();
        sentTo = sentTo != null ? Collections.unmodifiableList(sentTo) : List.of();
        removals = removals != null ? Collections.unmodifiableList(removals) : List.of();
    }
}