package io.mindspice.schemas;

import io.mindspice.enums.endpoints.Endpoint;
import java.util.Optional;


public record ApiResponse<T>(
        Optional<T> data,
        boolean success,
        String error,
        String address,
        String requestURI,
        Endpoint endpoint
) { }
