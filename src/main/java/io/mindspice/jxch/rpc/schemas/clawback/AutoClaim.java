package io.mindspice.jxch.rpc.schemas.clawback;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;


public record AutoClaim(
        @JsonProperty("tx_fee") BigInteger txFee,
        @JsonProperty("batch_size") int batchSize,
        @JsonProperty("success") boolean success,
        @JsonProperty("min_amount") BigInteger minAmount,
        @JsonProperty("error") String error,
        @JsonProperty("enabled") boolean enabled
) {
    public AutoClaim(long txFee, int batchSize, boolean success, long minAmount, String error, boolean enabled) {
        this(BigInteger.valueOf(txFee), batchSize, success, BigInteger.valueOf(minAmount), error, enabled);
    }
}