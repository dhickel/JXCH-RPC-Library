package io.mindspice.schemas.components;


public record RewardChain(
        String challenge_chain_sub_slot_hash,
        int deficit,
        VDF end_of_slot_vdf,
        String infused_challenge_chain_sub_slot_hash
) { }