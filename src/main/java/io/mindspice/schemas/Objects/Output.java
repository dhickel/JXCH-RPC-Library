package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;


public record Output(
        String data
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.OUTPUT;
    }
}
