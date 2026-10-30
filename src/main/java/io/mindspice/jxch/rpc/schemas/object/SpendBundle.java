package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public record SpendBundle(
        @JsonProperty("aggregated_signature") String aggregatedSignature,
        @JsonAlias({"coin_spends", "coin_solutions"})
        @JsonProperty("coin_spends") List<CoinSpend> coinSpends,
        @Deprecated @JsonInclude(JsonInclude.Include.NON_NULL) @JsonProperty("puzzle_reveal") String puzzleReveal,
        @Deprecated @JsonInclude(JsonInclude.Include.NON_NULL) @JsonProperty("solution") String solution
) {
    public SpendBundle {
        coinSpends = coinSpends == null ? List.of() : List.copyOf(coinSpends);
    }

    public SpendBundle(String aggregatedSignature, List<CoinSpend> coinSpends) {
        this(aggregatedSignature, coinSpends, null, null);
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