package io.mindspice.schemas;

import io.mindspice.enums.ChiaService;
import io.mindspice.enums.ResponseType;
import io.mindspice.enums.endpoints.Endpoint;


public interface ApiResponse {
     ChiaService getService();
     ChiaService.SubService getSubService();
     ResponseType getResponseType();

}
