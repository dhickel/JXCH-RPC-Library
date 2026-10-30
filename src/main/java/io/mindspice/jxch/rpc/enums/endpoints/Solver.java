package io.mindspice.jxch.rpc.enums.endpoints;

import io.mindspice.jxch.rpc.enums.ChiaService;

public enum Solver implements Endpoint {
    GET_STATE;
    @Override public ChiaService getService() { return ChiaService.SOLVER; }
    @Override public String getPath() { return "/get_state"; }
}
