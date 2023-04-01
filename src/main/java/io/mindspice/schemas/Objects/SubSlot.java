package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;


public record SubSlot(
        ChallengeChain challenge_chain,
        InfusedChallengeChain infused_challenge_chain,
        SubSlotProofs subSlotProofs,
        RewardChain reward_chain
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.SUB_SLOT;
    }
}