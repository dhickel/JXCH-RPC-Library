package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;


public record InfusedChallengeChain(
        VDF infused_challenge_chain_end_of_slot_vdf
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.INFUSED_CHALLENGE_CHAIN;
    }
}