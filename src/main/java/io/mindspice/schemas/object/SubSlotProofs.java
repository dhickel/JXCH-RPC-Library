package io.mindspice.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;


public record SubSlotProofs(
        @JsonProperty("challenge_chain_slot_proof") Proof challengeChainSlotProof,
        @JsonProperty("infused_challenge_chain_slot_proof") Proof infusedChallengeChainSlotProof,
        @JsonProperty("reward_chain_slot_proof") Proof rewardChainSlotProof
) { }