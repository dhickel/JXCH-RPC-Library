package io.mindspice.jxch.rpc.schemas.object;


import com.fasterxml.jackson.annotation.JsonProperty;


public record RewardChain(
        @JsonProperty("challenge_chain_sub_slot_hash") String challengeChainSubSlotHash,
        @JsonProperty("deficit") int deficit,
        @JsonProperty("end_of_slot_vdf") VDF endOfSlotVdf,
        @JsonProperty("infused_challenge_chain_sub_slot_hash") String infusedChallengeChainSubSlotHash
) { }