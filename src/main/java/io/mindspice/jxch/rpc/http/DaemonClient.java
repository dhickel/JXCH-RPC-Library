package io.mindspice.jxch.rpc.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.mindspice.jxch.rpc.enums.endpoints.Daemon;
import io.mindspice.jxch.rpc.util.JsonUtils;
import io.mindspice.jxch.rpc.schemas.daemon.Status;
import io.mindspice.jxch.rpc.util.RPCException;

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
            var req = new Request(Daemon.GET_STATUS, JsonUtils.newEmptyNodeAsBytes());
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
