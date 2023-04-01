package io.mindspice.schemas.Objects;


import io.mindspice.enums.ObjectType;

import io.mindspice.schemas.BlockChainObject;

import java.util.Collections;
import java.util.List;


public record Block(
        String header_hash,
        Proof challenge_chain_ip_proof,
        Proof challenge_chain_sp_proof,
        List<SubSlot> finished_sub_slots,
        Foliage foliage,
        FoliageTransactionBlock foliage_transaction_block,
        Proof infused_challenge_chain_ip_proof,
        RewardChainBlock reward_chain_block,
        Proof reward_chain_ip_proof,
        Proof reward_chain_sp_proof,
        String transactions_generator,
        List<String> transactions_generator_ref_list,
        TransactionInfo transactions_info
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.BLOCK;
    }


    public Block {
        finished_sub_slots = finished_sub_slots == null ? List.of() : Collections.unmodifiableList(finished_sub_slots);
        transactions_generator_ref_list = transactions_generator_ref_list == null
                ? List.of()
                : Collections.unmodifiableList(transactions_generator_ref_list);
    }
}