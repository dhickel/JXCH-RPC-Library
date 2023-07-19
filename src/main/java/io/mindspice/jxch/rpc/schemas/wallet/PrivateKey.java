package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;


public record PrivateKey(
        @JsonProperty("seed") String seed,
        @JsonProperty("farmer_pk") String farmerPk,
        @JsonProperty("fingerprint") long fingerprint,
        @JsonProperty("sk") String sk,
        @JsonProperty("pk") String pk,
        @JsonProperty("pool_pk") String poolPk
) {
}