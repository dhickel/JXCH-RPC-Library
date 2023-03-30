package io.mindspice.schemas.fullnode;

import io.mindspice.enums.ChiaService;
import io.mindspice.enums.ResponseType;
import io.mindspice.enums.endpoints.Endpoint;
import io.mindspice.enums.endpoints.FullNode;
import io.mindspice.schemas.ApiResponse;
import io.mindspice.schemas.components.*;

import java.util.List;


public record UnfinishedBlockHeaders (
        List<Headers> headers,
        boolean success,
        String error

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
        return ResponseType.BLOCK_HEADER;
    }
}

