package io.mindspice.jxch.rpc.enums.endpoints;

import io.mindspice.jxch.rpc.enums.ChiaService;


@Deprecated(forRemoval = false)
public enum Daemon implements Endpoint {
    GET_STATUS,
    GET_VERSION,
    IS_RUNNING,
    PING,
    // SHARED
    CLOSE_CONNECTION,
    GET_CONNECTIONS,
    GET_ROUTES,
    OPEN_CONNECTION,
    STOP_NODE,
    HEALTHZ;


    @Override
    public ChiaService getService() {
        return ChiaService.DAEMON;
    }


    @Override
    public String getPath() {
        throw new UnsupportedOperationException("The Chia daemon exposes WebSocket GET /, not HTTP RPC endpoint paths");
    }
}
