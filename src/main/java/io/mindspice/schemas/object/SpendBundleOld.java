package io.mindspice.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;


public record SpendBundleOld(
        @JsonProperty("aggregated_signature") String aggregatedSignature,
        @JsonProperty("coin_solutions") List<CoinSpend> coinSolutions,
        @JsonProperty("puzzle_reveal") String puzzleReveal,
        @JsonProperty("solution") String solution
) {
    public SpendBundleOld {
        coinSolutions = coinSolutions != null ? Collections.unmodifiableList(coinSolutions) : List.of();
    }


    public SpendBundleOld(SpendBundle spendBundle) {
        this(
                spendBundle.aggregatedSignature(),
                spendBundle.coinSpends(),
                spendBundle.puzzleReveal(),
                spendBundle.solution()
        );
    }
}