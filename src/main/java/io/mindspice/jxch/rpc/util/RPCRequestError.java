package io.mindspice.jxch.rpc.util;

public class RPCRequestError extends RPCException {
    public RPCRequestError(String message) {
        super(message);
    }

    public RPCRequestError(String message, Throwable cause) {
        super(message, cause);
    }
}
