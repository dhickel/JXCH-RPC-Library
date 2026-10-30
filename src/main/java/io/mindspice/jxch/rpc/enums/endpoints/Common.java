package io.mindspice.jxch.rpc.enums.endpoints;

import io.mindspice.jxch.rpc.enums.ChiaService;

public enum Common implements Endpoint {
    GET_NETWORK_INFO, GET_CONNECTIONS, OPEN_CONNECTION, CLOSE_CONNECTION, STOP_NODE,
    GET_ROUTES, GET_VERSION, HEALTHZ, GET_LOG_LEVEL, SET_LOG_LEVEL, RESET_LOG_LEVEL;

    @Override public ChiaService getService() {
        throw new IllegalStateException("Common RPC endpoints require an explicit ChiaService");
    }

    @Override public String getPath() { return "/" + name().toLowerCase(java.util.Locale.ROOT); }
}
