package io.mindspice.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;


public record RewardChainBlock(
        @JsonProperty("challenge_chain_ip_vdf") VDF challengeChainIpVdf,
        @JsonProperty("challenge_chain_sp_signature") String challengeChainSpSignature,
        @JsonProperty("challenge_chain_sp_vdf") VDF challengeChainSpVdf,
        @JsonProperty("height") long height,
        @JsonProperty("infused_challenge_chain_ip_vdf") Object infusedChallengeChainIpVdf,
        @JsonProperty("is_transaction_block") boolean isTransactionBlock,
        @JsonProperty("pos_ss_cc_challenge_hash") String posSsCcChallengeHash,
        @JsonProperty("proof_of_space") ProofOfSpace proofOfSpace,
        @JsonProperty("reward_chain_ip_vdf") VDF rewardChainIpVdf,
        @JsonProperty("reward_chain_sp_signature") String rewardChainSpSignature,
        @JsonProperty("reward_chain_sp_vdf") VDF rewardChainSpVdf,
        @JsonProperty("signage_point_index") int signagePointIndex,
        @JsonProperty("total_iters") BigInteger totalIters,
        @JsonProperty("weight") long weight
) { }