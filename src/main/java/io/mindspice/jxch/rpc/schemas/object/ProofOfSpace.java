package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProofOfSpace(
        @JsonProperty("challenge") String challenge,
        @JsonProperty("plot_public_key") String plotPublicKey,
        @JsonProperty("pool_contract_puzzle_hash") String poolContractPuzzleHash,
        @JsonProperty("pool_public_key") String poolPublicKey,
        @JsonProperty("proof") String proof,
        @JsonProperty("size") long size,
        @JsonProperty("version") int version,
        @JsonProperty("plot_index") int plotIndex,
        @JsonProperty("meta_group") int metaGroup,
        @JsonProperty("strength") int strength
) {
    @Deprecated
    public ProofOfSpace(String challenge, String plotPublicKey, Object poolContractPuzzleHash,
                        String poolPublicKey, String proof, long size) {
        this(challenge, plotPublicKey, (String) poolContractPuzzleHash, poolPublicKey, proof, size, 0, 0, 0, 0);
    }
}
