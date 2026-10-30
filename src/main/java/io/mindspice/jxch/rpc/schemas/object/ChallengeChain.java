package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;


public record ChallengeChain(
        @JsonProperty("challenge_chain_end_of_slot_vdf") VDF challengeChainEndOfSlotVdf,
        @JsonProperty("infused_challenge_chain_sub_slot_hash") String infusedChallengeChainSubSlotHash,
        @JsonProperty("new_difficulty") BigInteger newDifficulty,
        @JsonProperty("new_sub_slot_iters") BigInteger newSubSlotIters,
        @JsonProperty("subepoch_summary_hash") String subepochSummaryHash
) {
    public ChallengeChain(VDF challengeChainEndOfSlotVdf, String infusedChallengeChainSubSlotHash,
                          Number newDifficulty, Number newSubSlotIters, String subepochSummaryHash) {
        this(challengeChainEndOfSlotVdf, infusedChallengeChainSubSlotHash,
                newDifficulty == null ? null : new BigInteger(newDifficulty.toString()),
                newSubSlotIters == null ? null : new BigInteger(newSubSlotIters.toString()), subepochSummaryHash);
    }
}
