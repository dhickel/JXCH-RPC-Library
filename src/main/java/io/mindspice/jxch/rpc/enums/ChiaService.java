package io.mindspice.jxch.rpc.enums;

import io.mindspice.jxch.rpc.enums.endpoints.Daemon;
import io.mindspice.jxch.rpc.enums.endpoints.Endpoint;


public enum ChiaService {
    CRAWLER,
    DATA_LAYER,
    DAEMON,
    FARMER,
    FULL_NODE,
    HARVESTER,
    WALLET;


    ChiaService() { }

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
