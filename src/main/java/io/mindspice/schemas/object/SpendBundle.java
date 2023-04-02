package io.mindspice.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;


public record SpendBundle(
        @JsonProperty("aggregated_signature") String aggregatedSignature,
        @JsonProperty("coin_spends") List<CoinSpend> coinSpends,
        @JsonProperty("puzzle_reveal") String puzzleReveal,
        @JsonProperty("solution") String solution
) {
    public SpendBundle {
        coinSpends = coinSpends != null ? Collections.unmodifiableList(coinSpends) : List.of();
    }


    public SpendBundle(DeprecatedSpendBundle deprecatedSpendBundle) {
        this(
                deprecatedSpendBundle.aggregatedSignature(),
                deprecatedSpendBundle.coinSolutions(),
                deprecatedSpendBundle.puzzleReveal(),
                deprecatedSpendBundle.solution()
        );
    }
}