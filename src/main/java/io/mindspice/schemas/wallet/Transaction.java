package io.mindspice.schemas.wallet;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.mindspice.schemas.object.Coin;
import io.mindspice.schemas.object.SpendBundle;
import io.mindspice.util.StringListDeserializer;


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
        @JsonProperty("trade_id") Object tradeId,
        @JsonProperty("type") int type,
        @JsonProperty("name") String name,
        @JsonProperty("memos") JsonNode memos
) {
    public Transaction {
        additions = additions != null ? Collections.unmodifiableList(additions) : List.of();
        sentTo = sentTo != null ? Collections.unmodifiableList(sentTo) : List.of();
        removals = removals != null ? Collections.unmodifiableList(removals) : List.of();
    }
}