package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;


public record SignagePoint(
        VDF cc_vdf,
        Proof cc_proof,
        VDF rc_vdf, Proof rc_proof
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.SIGNAGE_POINT;
    }
}
