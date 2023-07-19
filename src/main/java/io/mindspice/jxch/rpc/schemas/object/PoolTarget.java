package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;


public record PoolTarget(
        @JsonProperty("max_height") int maxHeight,
        @JsonProperty("puzzle_hash") String puzzleHash
) { }
