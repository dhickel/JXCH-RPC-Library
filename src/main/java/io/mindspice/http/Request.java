package io.mindspice.http;

import io.mindspice.enums.ChiaService;
import io.mindspice.enums.endpoints.Endpoint;


public class Request {
    public final ChiaService service;
    public final String endpoint;
    public final byte[] data;

    public Request(Endpoint endpoint, byte[] data) {
        this.service = endpoint.getService();
        this.endpoint = endpoint.getPath();
        this.data = data;
    }
}
