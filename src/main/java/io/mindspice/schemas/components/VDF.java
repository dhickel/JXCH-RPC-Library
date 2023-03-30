package io.mindspice.schemas.components;


public record VDF(
        String challenge,
        long number_of_iterations,
        Output output
) { }