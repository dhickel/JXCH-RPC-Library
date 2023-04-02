package io.mindspice.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;


public record CoinSpend(
        @JsonProperty("coin") Coin coin,
        @JsonProperty("puzzle_reveal") String puzzleReveal,
        @JsonProperty("solution") String solution
) { }