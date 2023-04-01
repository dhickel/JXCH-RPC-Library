package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;

import java.util.Collections;
import java.util.List;


public record BlockHeader(
        List<SubSlot> finished_sub_slots,
        RewardChainBlock reward_chain_block,
        Proof challenge_chain_sp_proof,
        Proof reward_chain_sp_proof,
        Foliage foliage,
        FoliageTransactionBlock foliage_transaction_block,
        String transactions_filter
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.BLOCK_HEADER;
    }


    public BlockHeader {
        finished_sub_slots = finished_sub_slots == null ? List.of() : Collections.unmodifiableList(finished_sub_slots);
    }

}