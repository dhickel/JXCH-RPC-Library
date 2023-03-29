package io.mindspice.schemas.daemon;

import io.mindspice.enums.endpoints.Daemon;
import io.mindspice.enums.endpoints.Endpoint;


public record Status(boolean genesis_initialized, boolean success, String Error) {
    private static final Endpoint endpoint = Daemon.GET_STATUS;


    public Endpoint getEndpoint() { return endpoint; }
}
