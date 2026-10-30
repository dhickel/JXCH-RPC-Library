package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public record SubEpochSummary(
        @JsonProperty("prev_subepoch_summary_hash") String prevSubepochSummaryHash,
        @JsonProperty("reward_chain_hash") String rewardChainHash,
        @JsonProperty("num_blocks_overflow") int numBlocksOverflow,
        @JsonProperty("new_difficulty") BigInteger newDifficulty,
        @JsonProperty("new_sub_slot_iters") BigInteger newSubSlotIters,
        @JsonProperty("challenge_merkle_root") String challengeMerkleRoot
) { }
