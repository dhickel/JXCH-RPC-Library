package io.mindspice.schemas.components;

public record SubSlot(
        ChallengeChain challenge_chain,
        InfusedChallengeChain infused_challenge_chain,
        Proofs proofs,
        RewardChain reward_chain
) {}