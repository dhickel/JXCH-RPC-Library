package io.mindspice.schemas.components;

public record ProofOfSpace(
        String challenge,
        String plot_public_key,
        Object pool_contract_puzzle_hash,
        String pool_public_key,
        String proof,
        long size
) { }