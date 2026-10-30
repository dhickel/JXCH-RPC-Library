package io.mindspice.jxch.rpc.enums.endpoints;

import io.mindspice.jxch.rpc.enums.ChiaService;

public enum Crawler implements Endpoint {
    GET_PEER_COUNTS, GET_IPS_AFTER_TIMESTAMP;
    @Override public ChiaService getService() { return ChiaService.CRAWLER; }
    @Override public String getPath() { return "/" + name().toLowerCase(java.util.Locale.ROOT); }
}
