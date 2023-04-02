package io.mindspice.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;


public record NpcResult(
        @JsonProperty("conds") Conditions conds,
        @JsonProperty("cost") long cost,
        @JsonProperty("error") String error
) { }