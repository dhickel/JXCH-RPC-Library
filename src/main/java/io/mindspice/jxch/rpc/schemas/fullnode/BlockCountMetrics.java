package io.mindspice.jxch.rpc.schemas.fullnode;

import com.fasterxml.jackson.annotation.JsonProperty;



public record BlockCountMetrics(
        @JsonProperty("compact_blocks") int compactBlocks,
        @JsonProperty("uncompact_blocks") int uncompactBlocks,
        @JsonProperty("hint_count") int hintCount
) { }