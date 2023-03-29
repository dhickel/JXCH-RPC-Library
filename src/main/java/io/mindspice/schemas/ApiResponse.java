package io.mindspice.schemas;

import io.mindspice.enums.ChiaService;
import io.mindspice.enums.endpoints.Endpoint;


public interface ApiResponse {
    public Endpoint getEndpoint();
    public ChiaService getService();
}
