package io.mindspice.jxch.rpc.http;

import io.mindspice.jxch.rpc.schemas.daemon.Status;
import io.mindspice.jxch.rpc.util.RPCException;


@Deprecated(forRemoval = false)
public class DaemonClient {
    public DaemonClient(RPCClient rpcClient) { }

    public static Request exit() {
        throw new UnsupportedOperationException("The Chia daemon requires WebSocket transport");
    }

    public byte[] get_status_bytes() throws RPCException {
        throw new RPCException("The Chia daemon get_status command requires WebSocket transport");
    }

    public Status get_status() throws RPCException {
        throw new RPCException("The Chia daemon get_status command requires WebSocket transport (GET / upgrade); HTTP RPC is not supported");
    }

}
