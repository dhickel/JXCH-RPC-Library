package io.mindspice.jxch.rpc.schemas.custom;

import com.fasterxml.jackson.annotation.JsonProperty;


public record InclusionCost(
        @JsonProperty("cost") long cost,
        @JsonProperty("room_in_mempool") boolean roomInMempool,
        @JsonProperty("fee_to_spend") long feeToSpend,
        @JsonProperty("min_valid_fee") long minValidFee,
        @JsonProperty("success") boolean success,
        @JsonProperty("error") String error

) { }