package io.mindspice.schemas.components;

public record ChallengeChain(
        VDF challenge_chain_end_of_slot_vdf,
        String infused_challenge_chain_sub_slot_hash,
        Integer new_difficulty,
        Integer new_sub_slot_iters,
        String subepoch_summary_hash
) { }
