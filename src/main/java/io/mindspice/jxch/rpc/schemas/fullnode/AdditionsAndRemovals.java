package io.mindspice.jxch.rpc.schemas.fullnode;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.jxch.rpc.schemas.object.CoinRecord;

import java.util.Collections;
import java.util.List;


public record AdditionsAndRemovals(
        @JsonProperty("additions") List<CoinRecord> additions,
        @JsonProperty("removals") List<CoinRecord> removals,
        @JsonProperty("success") boolean success,
        @JsonProperty("error") String error
) {
    public AdditionsAndRemovals {
        additions = additions != null ? Collections.unmodifiableList(additions) : List.of();
        removals = removals != null ? Collections.unmodifiableList(removals) : List.of();
    }
    public AdditionsAndRemovals(List<CoinRecord> additions, List<CoinRecord> removals) {
        this(additions, removals, true, "");
    }
}