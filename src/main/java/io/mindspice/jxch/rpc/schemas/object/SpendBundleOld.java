package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


@Deprecated
public record SpendBundleOld(
        @JsonProperty("aggregated_signature") String aggregatedSignature,
        @JsonProperty("coin_solutions") List<CoinSpend> coinSolutions,
        @JsonProperty("puzzle_reveal") String puzzleReveal,
        @JsonProperty("solution") String solution
) {
    public SpendBundleOld {
        coinSolutions = coinSolutions == null ? List.of() : List.copyOf(coinSolutions);
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