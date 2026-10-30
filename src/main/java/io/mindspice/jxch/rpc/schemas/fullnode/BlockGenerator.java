package io.mindspice.jxch.rpc.schemas.fullnode;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.jxch.rpc.schemas.object.Coin;

import java.math.BigInteger;
import java.util.List;

public record BlockGenerator(
        @JsonProperty("generator") String generator,
        @JsonProperty("refs") List<Long> refs,
        @JsonProperty("additions") List<Coin> additions,
        @JsonProperty("removals") List<Coin> removals,
        @JsonProperty("sig") String sig,
        @JsonProperty("cost") BigInteger cost
) {
    public BlockGenerator {
        refs = refs == null ? List.of() : List.copyOf(refs);
        additions = additions == null ? List.of() : List.copyOf(additions);
        removals = removals == null ? List.of() : List.copyOf(removals);
    }
}
