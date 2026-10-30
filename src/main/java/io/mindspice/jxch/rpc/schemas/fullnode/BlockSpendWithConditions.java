package io.mindspice.jxch.rpc.schemas.fullnode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.schemas.object.CoinSolution;

import java.util.List;

public record BlockSpendWithConditions(
        @JsonProperty("coin_spend") CoinSolution coinSpend,
        @JsonProperty("conditions") List<JsonNode> conditions
) {
    public BlockSpendWithConditions {
        conditions = conditions == null ? List.of() : List.copyOf(conditions);
    }
}
