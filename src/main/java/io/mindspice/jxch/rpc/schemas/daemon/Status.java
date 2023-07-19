package io.mindspice.jxch.rpc.schemas.daemon;

import io.mindspice.jxch.rpc.enums.endpoints.Daemon;
import io.mindspice.jxch.rpc.enums.endpoints.Endpoint;


public record Status(boolean genesis_initialized, boolean success, String Error) {
    private static final Endpoint endpoint = Daemon.GET_STATUS;


    public Endpoint getEndpoint() { return endpoint; }
}
