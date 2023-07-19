package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;


public record Conditions(
        @JsonProperty("addition_amount") long additionAmount,
        @JsonProperty("agg_sig_unsafe") List<String> aggSigUnsafe, //FIXME not sure what this is, returns []
        @JsonProperty("before_height_absolute") long beforeHeightAbsolute,
        @JsonProperty("before_seconds_absolute") long beforeSecondsAbsolute,
        @JsonProperty("cost") long cost,
        @JsonProperty("height_absolute") long heightAbsolute,
        @JsonProperty("removal_amount") long removalAmount,
        @JsonProperty("reserve_fee") long reserveFee,
        @JsonProperty("seconds_absolute") long secondsAbsolute,
        @JsonProperty("spends") List<Spend> spends
) {
    public Conditions {
        aggSigUnsafe = aggSigUnsafe != null ? Collections.unmodifiableList(aggSigUnsafe) : List.of();
        spends = spends != null ? Collections.unmodifiableList(spends) : List.of();
    }
}