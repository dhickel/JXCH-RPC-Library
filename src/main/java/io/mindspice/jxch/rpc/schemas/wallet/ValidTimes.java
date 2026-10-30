package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;

public record ValidTimes(
        @JsonProperty("min_time") BigInteger minTime,
        @JsonProperty("min_height") Long minHeight,
        @JsonProperty("max_time") BigInteger maxTime,
        @JsonProperty("max_height") Long maxHeight,
        @JsonProperty("min_secs_since_created") BigInteger minSecsSinceCreated,
        @JsonProperty("min_blocks_since_created") Long minBlocksSinceCreated,
        @JsonProperty("max_secs_after_created") BigInteger maxSecsAfterCreated,
        @JsonProperty("max_blocks_after_created") Long maxBlocksAfterCreated
) { }
