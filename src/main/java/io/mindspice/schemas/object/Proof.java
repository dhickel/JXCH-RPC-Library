package io.mindspice.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;


public record Proof(
        @JsonProperty("normalized_to_identity") boolean normalizedToIdentity,
        @JsonProperty("witness") String witness,
        @JsonProperty("witness_type") int witnessType
) { }