package io.mindspice.jxch.rpc.enums.endpoints;

import io.mindspice.jxch.rpc.enums.ChiaService;


public interface Endpoint {
     ChiaService getService();
     String getPath();
}
