package io.mindspice.schemas.components;

import java.util.List;


public record Headers(
        List<SubSlot> finished_sub_slots,
        RewardChainBlock reward_chain_block,
        Proof challenge_chain_sp_proof,
        Proof reward_chain_sp_proof,
        Foliage foliage,
        FoliageTransactionBlock foliage_transaction_block,
        String transactions_filter
){}