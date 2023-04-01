package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;


public record Proof(
        boolean normalized_to_identity,
        String witness,
        int witness_type
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.PROOF;
    }
}
