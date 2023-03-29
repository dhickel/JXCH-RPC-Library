package io.mindspice.enums.endpoints;

import io.mindspice.enums.ChiaService;


public interface Endpoint {
     ChiaService getService();
     String getPath();
}
