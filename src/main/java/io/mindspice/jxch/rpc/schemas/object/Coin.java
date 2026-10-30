package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;


public record Coin (
        @JsonProperty("parent_coin_info") String parentCoinInfo,
        @JsonProperty("puzzle_hash") String puzzleHash,
        @JsonProperty("amount") BigInteger amount
) {
    public Coin(String parentCoinInfo, String puzzleHash, long amount) {
        this(parentCoinInfo, puzzleHash, BigInteger.valueOf(amount));
    }
}