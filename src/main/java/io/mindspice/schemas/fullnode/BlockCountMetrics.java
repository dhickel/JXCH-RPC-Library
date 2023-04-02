package io.mindspice.schemas.fullnode;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.enums.ChiaService;
import io.mindspice.enums.endpoints.Endpoint;
import io.mindspice.enums.endpoints.FullNode;
import io.mindspice.schemas.ApiResponse;


public record BlockCountMetrics(
        @JsonProperty("compact_blocks") int compactBlocks,
        @JsonProperty("uncompact_blocks") int uncompactBlocks,
        @JsonProperty("hint_count") int hintCount
) { }