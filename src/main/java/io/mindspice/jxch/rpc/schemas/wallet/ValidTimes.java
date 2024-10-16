package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;


public record ValidTimes(
        @JsonProperty("max_secs_after_created") long maxSecsAfterCreated,
        @JsonProperty("max_blocks_after_created") long maxBlocksAfterCreated,
        @JsonProperty("min_height") long minHeight,
        @JsonProperty("max_height") long maxHeight,
        @JsonProperty("min_secs_since_created") long minSecsSinceCreated,
        @JsonProperty("max_time") long maxTime,
        @JsonProperty("min_blocks_since_created") long minBlocksSinceCreated,
        @JsonProperty("min_time") long minTime
) {
}