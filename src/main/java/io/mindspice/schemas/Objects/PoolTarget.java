package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;


public record PoolTarget(
        int max_height,
        String puzzle_hash
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.POOL_TARGET;
    }
}

