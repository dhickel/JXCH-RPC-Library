package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;


public record SpendBundle(
        @JsonProperty("aggregated_signature") String aggregatedSignature,
        @JsonAlias({"coin_spends", "coin_solutions"})
        @JsonProperty("coin_spends") List<CoinSpend> coinSpends,
        @JsonProperty("puzzle_reveal") String puzzleReveal,
        @JsonProperty("solution") String solution
) {
    public SpendBundle {
        coinSpends = coinSpends != null ? Collections.unmodifiableList(coinSpends) : List.of();
    }

    public SpendBundle(SpendBundleOld spendBundleOld) {
        this(
                spendBundleOld.aggregatedSignature(),
                spendBundleOld.coinSolutions(),
                spendBundleOld.puzzleReveal(),
                spendBundleOld.solution()
        );
    }
}