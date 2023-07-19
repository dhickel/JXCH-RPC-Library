package io.mindspice.jxch.rpc.http;

import io.mindspice.jxch.rpc.enums.ChiaService;
import io.mindspice.jxch.rpc.enums.endpoints.Endpoint;


public class Request {
    public final ChiaService service;
    public final String endpoint;
    public final byte[] data;

    public Request(Endpoint endpoint, byte[] data) {
        this.service = endpoint.getService();
        this.endpoint = endpoint.getPath();
        this.data = data;
    }

    public Request(ChiaService service, Endpoint endpoint, byte[] data) {
        this.service = service;
        this.endpoint = endpoint.getPath();
        this.data = data;
    }
}
