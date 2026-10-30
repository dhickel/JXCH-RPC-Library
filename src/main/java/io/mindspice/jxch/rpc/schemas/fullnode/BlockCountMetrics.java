package io.mindspice.jxch.rpc.schemas.fullnode;

import com.fasterxml.jackson.annotation.JsonProperty;



public record BlockCountMetrics(
        @JsonProperty("compact_blocks") long compactBlocks,
        @JsonProperty("uncompact_blocks") long uncompactBlocks,
        @JsonProperty("hint_count") long hintCount
) { }