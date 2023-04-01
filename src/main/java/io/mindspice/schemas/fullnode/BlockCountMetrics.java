package io.mindspice.schemas.fullnode;

import io.mindspice.enums.ChiaService;

import io.mindspice.enums.endpoints.Endpoint;
import io.mindspice.enums.endpoints.FullNode;
import io.mindspice.schemas.ApiResponse;


public record BlockCountMetrics(
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
    public Endpoint getEndPoint() {
        return FullNode.GET_BLOCK_COUNT_METRICS;
    }
}
