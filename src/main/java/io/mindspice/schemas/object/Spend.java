package io.mindspice.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;


public record Spend(
        @JsonProperty("agg_sig_me") List<List<String>> aggSigMe,
        @JsonProperty("before_height_relative") int beforeHeightRelative,
        @JsonProperty("before_seconds_relative") int beforeSecondsRelative,
        @JsonProperty("birth_height") int birthHeight,
        @JsonProperty("birth_seconds") int birthSeconds,
        @JsonProperty("coin_id") String coinId,
        @JsonProperty("create_coin") List<List<String>> createCoin,
        @JsonProperty("flags") int flags,
        @JsonProperty("height_relative") int heightRelative,
        @JsonProperty("puzzle_hash") String puzzleHash,
        @JsonProperty("seconds_relative") long secondsRelative
) {
    public Spend {
        aggSigMe = aggSigMe != null ? Collections.unmodifiableList(aggSigMe) : List.of();
        createCoin = createCoin != null ? Collections.unmodifiableList(createCoin) : List.of();
    }
}