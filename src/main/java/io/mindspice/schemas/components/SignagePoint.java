package io.mindspice.schemas.components;

public record SignagePoint(
        VDF cc_vdf,
        Proof cc_proof,
        VDF rc_vdf, Proof rc_proof
) { }
