package io.mindspice.jxch.rpc.http;

import io.mindspice.jxch.rpc.enums.ChiaService;

public class TimelordAPI extends ChiaAPI {
    public TimelordAPI(RPCClient client) { super(client, ChiaService.TIMELORD); }
}
