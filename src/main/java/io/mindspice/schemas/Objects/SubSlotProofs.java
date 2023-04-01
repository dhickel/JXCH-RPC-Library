package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;


public record SubSlotProofs(
        Proof challenge_chain_slot_proof,
        Proof infused_challenge_chain_slot_proof,
        Proof reward_chain_slot_proof
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.SUB_SLOT_PROOFS;
    }
}
