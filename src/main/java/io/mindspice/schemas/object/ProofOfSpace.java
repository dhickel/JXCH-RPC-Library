package io.mindspice.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;


public record ProofOfSpace(
        @JsonProperty("challenge") String challenge,
        @JsonProperty("plot_public_key") String plotPublicKey,
        @JsonProperty("pool_contract_puzzle_hash") Object poolContractPuzzleHash,
        @JsonProperty("pool_public_key") String poolPublicKey,
        @JsonProperty("proof") String proof,
        @JsonProperty("size") long size
) { }