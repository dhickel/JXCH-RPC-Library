package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;


public record SubSlot(
        @JsonProperty("challenge_chain") ChallengeChain challengeChain,
        @JsonProperty("infused_challenge_chain") InfusedChallengeChain infusedChallengeChain,
        @JsonProperty("sub_slot_proofs") SubSlotProofs subSlotProofs,
        @JsonProperty("reward_chain") RewardChain rewardChain
) { }