package io.mindspice.schemas.object;



public record BasicResponse(
        String status,
        boolean success,
        String error
) { }
