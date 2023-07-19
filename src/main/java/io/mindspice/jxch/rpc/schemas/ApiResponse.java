package io.mindspice.jxch.rpc.schemas;

import io.mindspice.jxch.rpc.enums.endpoints.Endpoint;
import java.util.Optional;


public record ApiResponse<T>(
        Optional<T> data,
        boolean success,
        String error,
        String address,
        String requestURI,
        Endpoint endpoint
) { }
