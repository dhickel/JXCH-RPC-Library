package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


public record NpcResult(
        @JsonProperty("conds") Conditions conds,
        @Deprecated @JsonInclude(JsonInclude.Include.NON_DEFAULT) @JsonProperty("cost") long cost,
        @JsonAlias("error") @JsonProperty("Error") String error
) {
    public NpcResult(Conditions conds, String error) {
        this(conds, 0, error);
    }
}