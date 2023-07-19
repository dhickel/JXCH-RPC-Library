package io.mindspice.jxch.rpc.enums.endpoints;

import io.mindspice.jxch.rpc.enums.ChiaService;


public enum Shared implements Endpoint{
    CLOSE_CONNECTION,
    GET_CONNECTIONS,
    GET_ROUTES,
    OPEN_CONNECTION,
    STOP_NODE,
    HEALTHZ;

    @Override
    public ChiaService getService() {
        return null;
    }

    @Override
    public String getPath() {
        return "/" + name().toLowerCase();
    }
}
