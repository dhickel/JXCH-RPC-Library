package io.mindspice.enums.endpoints;

import io.mindspice.enums.ChiaService;


public enum FullNode implements Endpoint {
    GET_ADDITIONS_AND_REMOVALS,
    GET_BLOCK,
    GET_BLOCK_COUNT_METRICS,
    GET_BLOCK_RECORD,
    GET_BLOCK_RECORD_BY_HEIGHT,
    GET_BLOCK_RECORDS,
    GET_BLOCK_SPENDS,
    GET_BLOCKS,
    GET_RECENT_SIGNAGE_POINT_OR_EOS,
    GET_UNFINISHED_BLOCKS;


    @Override
    public ChiaService getService() {
        return ChiaService.FULL_NODE;
    }


    @Override
    public String getPath() {
        return "/" + name().toLowerCase();
    }
}
