package io.mindspice.schemas;

import java.util.Optional;


public record ApiResponse<T>(Optional<T> data, boolean success, String error, String requestURI) {
}
