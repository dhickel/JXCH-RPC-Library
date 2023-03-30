package io.mindspice.schemas.components;

public record Proof(
        boolean normalized_to_identity,
        String witness,
        int witness_type
) { }
