package io.mindspice.enums;

public enum ChiaService {
    CRAWLER,
    DATA_LAYER,
    DAEMON,
    FARMER,
    FULL_NODE,
    HARVESTER,
    WALLET;

    public enum SubService {
        BLOCKS,
        COINS,
        SHARED,
        METRICS,
        MEMPOOL,
        FEES,
        FARMER,



    }
}
