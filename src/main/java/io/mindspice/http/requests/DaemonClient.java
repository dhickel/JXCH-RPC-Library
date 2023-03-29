package io.mindspice.http.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.mindspice.enums.endpoints.Daemon;
import io.mindspice.http.RPCClient;
import io.mindspice.schemas.daemon.*;
import io.mindspice.util.JsonUtils;
import io.mindspice.util.RPCException;

import java.io.IOException;


public class DaemonClient {
    private final RPCClient client;

    public DaemonClient(RPCClient rpcClient) {
        client = rpcClient;
    }


    public static Request exit() {
        return null;
    }

    public byte[] get_status_bytes() throws RPCException {
        try {
            var req = new Request(Daemon.GET_STATUS, JsonUtils.writeBytes(JsonUtils.emptyNode()));
            return client.makeRequest(req);
        } catch (JsonProcessingException e) {
            throw new RPCException("Error writing request JSON", e);
        }
    }

    public Status get_status() throws RPCException {
        try {
            return JsonUtils.readJson(get_status_bytes(), Status.class);
        } catch (IOException e) {
            throw new RPCException("Error reading request JSON", e);
        }
    }

}
