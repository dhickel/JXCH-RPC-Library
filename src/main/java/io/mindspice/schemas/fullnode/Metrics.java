package io.mindspice.schemas.fullnode;

import io.mindspice.enums.ChiaService;
import io.mindspice.enums.ResponseType;
import io.mindspice.schemas.ApiResponse;


public record Metrics(
        int compact_blocks,
        int uncompact_blocks,
        int hint_count

) implements ApiResponse {


    @Override
    public ChiaService getService() {
        return ChiaService.FULL_NODE;
    }


    @Override
    public ChiaService.SubService getSubService() {
        return ChiaService.SubService.BLOCKS;
    }


    @Override
    public ResponseType getResponseType() {
        return ResponseType.METRICS;
    }
}
