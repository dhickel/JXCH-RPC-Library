package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;


public record CoinRecord(
        @JsonProperty("coin") Coin coin,
        @JsonProperty("confirmed_block_index") long confirmedBlockIndex,
        @JsonProperty("spent_block_index") long spentBlockIndex,
        @JsonProperty("spent") boolean spent,
        @JsonProperty("coinbase") boolean coinbase,
        @JsonProperty("timestamp") BigInteger timestamp
) {
    public CoinRecord(Coin coin, int confirmedBlockIndex, int spentBlockIndex, boolean spent,
                      boolean coinbase, long timestamp) {
        this(coin, confirmedBlockIndex, spentBlockIndex, spent, coinbase, BigInteger.valueOf(timestamp));
    }
}

