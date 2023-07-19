package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;


public record SignagePoint(
        @JsonProperty("cc_vdf") VDF ccVdf,
        @JsonProperty("cc_proof") Proof ccProof,
        @JsonProperty("rc_vdf") VDF rcVdf,
        @JsonProperty("rc_proof") Proof rcProof
) { }