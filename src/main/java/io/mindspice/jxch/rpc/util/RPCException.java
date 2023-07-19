package io.mindspice.jxch.rpc.util;

public class RPCException extends Exception{
    public RPCException(String message) {
        super(message);
    }


    public RPCException(String message, Throwable cause) {
        super(message, cause);
    }
}
