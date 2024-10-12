package io.mindspice.jxch.rpc.schemas.fullnode;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.jxch.rpc.schemas.object.CoinSpend;


public record SpendWithConditions(
        @JsonProperty("coin_spend") CoinSpend coinSpend,
        @JsonProperty("conditions") List<Condition> conditions
) {
    public SpendWithConditions {
        conditions = conditions != null ? Collections.unmodifiableList(conditions) : List.of();
    }
}