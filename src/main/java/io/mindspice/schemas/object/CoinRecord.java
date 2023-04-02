package io.mindspice.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;


public record CoinRecord(
        @JsonProperty("coin") Coin coin,
        @JsonProperty("confirmed_block_index") int confirmedBlockIndex,
        @JsonProperty("spent_block_index") int spentBlockIndex,
        @JsonProperty("spent") boolean spent,
        @JsonProperty("coinbase") boolean coinbase,
        @JsonProperty("timestamp") long timestamp
) { }

