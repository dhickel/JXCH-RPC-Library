package io.mindspice.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;


public record ChallengeChain(
        @JsonProperty("challenge_chain_end_of_slot_vdf") VDF challengeChainEndOfSlotVdf,
        @JsonProperty("infused_challenge_chain_sub_slot_hash") String infusedChallengeChainSubSlotHash,
        @JsonProperty("new_difficulty") Integer newDifficulty,
        @JsonProperty("new_sub_slot_iters") Integer newSubSlotIters,
        @JsonProperty("subepoch_summary_hash") String subepochSummaryHash
) { }
