package io.mindspice.schemas.object;


import com.fasterxml.jackson.annotation.JsonProperty;


public record VDF(
        @JsonProperty("challenge") String challenge,
        @JsonProperty("number_of_iterations") long numberOfIterations,
        @JsonProperty("output") Output output
) { }