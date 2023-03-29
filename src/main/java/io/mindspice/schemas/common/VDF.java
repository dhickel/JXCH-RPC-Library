package io.mindspice.schemas.common;

import io.mindspice.schemas.fullnode.Block;


public record VDF(
        String challenge,
        long number_of_iterations,
        Output output
) { }