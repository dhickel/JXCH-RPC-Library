package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;

import java.util.List;


public record NpcResult(
        Conditions conds,
        long cost,
        String error
) implements BlockChainObject {

    @Override
    public ObjectType getObjectType() {
        return ObjectType.NPC_RESULT;
    }
}
