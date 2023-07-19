package io.mindspice.jxch.rpc.enums.endpoints;

import io.mindspice.jxch.rpc.enums.ChiaService;


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
    GET_UNFINISHED_BLOCK_HEADERS,
    GET_COIN_RECORD_BY_NAME,
    GET_COIN_RECORDS_BY_HINT,
    GET_COIN_RECORDS_BY_NAMES,
    GET_COIN_RECORDS_BY_PARENT_IDS,
    GET_COIN_RECORDS_BY_PUZZLE_HASH,
    GET_COIN_RECORDS_BY_PUZZLE_HASHES,
    GET_PUZZLE_AND_SOLUTION,
    PUSH_TX,
    GET_BLOCKCHAIN_STATE,
    GET_NETWORK_INFO,
    GET_NETWORK_SPACE,
    GET_ALL_MEMPOOL_ITEMS,
    GET_ALL_MEMPOOL_TX_IDS,
    GET_MEMPOOL_ITEM_BY_TX_ID,
    GET_FEE_ESTIMATE;


    @Override
    public ChiaService getService() {
        return ChiaService.FULL_NODE;
    }


    @Override
    public String getPath() {
        return "/" + name().toLowerCase();
    }
}
