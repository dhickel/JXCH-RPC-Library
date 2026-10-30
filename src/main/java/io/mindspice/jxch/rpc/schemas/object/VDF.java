package io.mindspice.jxch.rpc.schemas.object;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;


public record VDF(
        @JsonProperty("challenge") String challenge,
        @JsonProperty("number_of_iterations") BigInteger numberOfIterations,
        @JsonProperty("output") Output output
) {
    public VDF(String challenge, long numberOfIterations, Output output) {
        this(challenge, BigInteger.valueOf(numberOfIterations), output);
    }
}