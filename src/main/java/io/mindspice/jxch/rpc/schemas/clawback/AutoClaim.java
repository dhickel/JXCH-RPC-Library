package io.mindspice.jxch.rpc.schemas.clawback;

import com.fasterxml.jackson.annotation.JsonProperty;


public record AutoClaim(
        @JsonProperty("tx_fee") long txFee,
        @JsonProperty("batch_size") int batchSize,
        @JsonProperty("success") boolean success,
        @JsonProperty("min_amount") long minAmount,
        @JsonProperty("error") String error,
        @JsonProperty("enabled") boolean enabled
) {
}