package io.mindspice.schemas.Objects;


import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;


public record VDF(
        String challenge,
        long number_of_iterations,
        Output output
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.VDF;
    }
}