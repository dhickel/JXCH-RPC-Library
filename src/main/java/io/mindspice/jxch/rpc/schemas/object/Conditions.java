package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.List;

public record Conditions(
        @JsonProperty("addition_amount") BigInteger additionAmount,
        @JsonProperty("agg_sig_unsafe") List<List<String>> aggSigUnsafe,
        @JsonProperty("before_height_absolute") Long beforeHeightAbsolute,
        @JsonProperty("before_seconds_absolute") BigInteger beforeSecondsAbsolute,
        @JsonProperty("cost") BigInteger cost,
        @JsonProperty("height_absolute") long heightAbsolute,
        @JsonProperty("removal_amount") BigInteger removalAmount,
        @JsonProperty("reserve_fee") BigInteger reserveFee,
        @JsonProperty("seconds_absolute") BigInteger secondsAbsolute,
        @JsonProperty("spends") List<Spend> spends,
        @JsonProperty("validated_signature") boolean validatedSignature,
        @JsonProperty("execution_cost") BigInteger executionCost,
        @JsonProperty("condition_cost") BigInteger conditionCost,
        @JsonProperty("num_atoms") long numAtoms,
        @JsonProperty("num_pairs") long numPairs,
        @JsonProperty("heap_size") long heapSize
) {
    public Conditions {
        aggSigUnsafe = aggSigUnsafe == null ? List.of() : aggSigUnsafe.stream().map(List::copyOf).toList();
        spends = spends == null ? List.of() : List.copyOf(spends);
    }

    @Deprecated
    public Conditions(long additionAmount, List<String> aggSigUnsafe, long beforeHeightAbsolute,
                      long beforeSecondsAbsolute, long cost, long heightAbsolute, long removalAmount,
                      long reserveFee, long secondsAbsolute, List<Spend> spends) {
        this(BigInteger.valueOf(additionAmount), legacySignatures(aggSigUnsafe), beforeHeightAbsolute,
                BigInteger.valueOf(beforeSecondsAbsolute), BigInteger.valueOf(cost), heightAbsolute,
                BigInteger.valueOf(removalAmount), BigInteger.valueOf(reserveFee), BigInteger.valueOf(secondsAbsolute),
                spends, false, null, null, 0, 0, 0);
    }

    private static List<List<String>> legacySignatures(List<String> signatures) {
        if (signatures != null && !signatures.isEmpty()) {
            throw new IllegalArgumentException("agg_sig_unsafe requires public-key/message pairs, not strings");
        }
        return List.of();
    }
}
