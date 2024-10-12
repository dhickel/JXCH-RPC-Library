package io.mindspice.jxch.rpc.schemas.fullnode;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


public record Condition(
        @JsonProperty("vars") List<String> vars,
        @JsonProperty("opcode") int opcode
) {

    public Condition {
        vars = vars != null ? Collections.unmodifiableList(vars) : List.of();
    }
}