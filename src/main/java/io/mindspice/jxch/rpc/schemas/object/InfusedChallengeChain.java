package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;


public record InfusedChallengeChain(
        @JsonProperty("infused_challenge_chain_end_of_slot_vdf") VDF infusedChallengeChainEndOfSlotVdf
) { }