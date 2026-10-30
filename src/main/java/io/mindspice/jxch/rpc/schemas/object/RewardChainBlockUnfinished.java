package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public record RewardChainBlockUnfinished(
        @JsonProperty("total_iters") BigInteger totalIters,
        @JsonProperty("signage_point_index") int signagePointIndex,
        @JsonProperty("pos_ss_cc_challenge_hash") String posSsCcChallengeHash,
        @JsonProperty("proof_of_space") ProofOfSpace proofOfSpace,
        @JsonProperty("challenge_chain_sp_vdf") VDF challengeChainSpVdf,
        @JsonProperty("challenge_chain_sp_signature") String challengeChainSpSignature,
        @JsonProperty("reward_chain_sp_vdf") VDF rewardChainSpVdf,
        @JsonProperty("reward_chain_sp_signature") String rewardChainSpSignature
) { }
