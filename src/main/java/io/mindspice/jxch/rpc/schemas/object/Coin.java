package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;


public record Coin (
        @JsonProperty("parent_coin_info") String parentCoinInfo,
        @JsonProperty("puzzle_hash") String puzzleHash,
        @JsonProperty("amount") long amount
) { }