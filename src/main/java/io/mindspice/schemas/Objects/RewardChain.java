package io.mindspice.schemas.Objects;


import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;


public record RewardChain(
        String challenge_chain_sub_slot_hash,
        int deficit,
        VDF end_of_slot_vdf,
        String infused_challenge_chain_sub_slot_hash
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.REWARD_CHAIN;
    }
}