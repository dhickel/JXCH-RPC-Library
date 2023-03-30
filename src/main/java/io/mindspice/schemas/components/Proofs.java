package io.mindspice.schemas.components;

public record Proofs(
        Proof challenge_chain_slot_proof,
        Proof infused_challenge_chain_slot_proof,
        Proof reward_chain_slot_proof
) { }
