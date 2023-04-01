package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;


public record ChallengeChain(
        VDF challenge_chain_end_of_slot_vdf,
        String infused_challenge_chain_sub_slot_hash,
        Integer new_difficulty,
        Integer new_sub_slot_iters,
        String subepoch_summary_hash
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.CHALLENGE_CHAIN;
    }
}
