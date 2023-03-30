package io.mindspice.schemas.fullnode;

import io.mindspice.enums.ChiaService;
import io.mindspice.enums.ResponseType;
import io.mindspice.schemas.ApiResponse;
import io.mindspice.schemas.components.*;


public record SignagePointOrEOS(
        long time_received,
        boolean reverted,
        boolean success,
        String error,
        SignagePoint signage_point,
        SubSlot eos

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
        return signage_point == null ? ResponseType.END_OF_SUB_SLOT : ResponseType.SIGNAGE_POINT;
    }
}
