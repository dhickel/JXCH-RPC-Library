package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;


public record ProofOfSpace(
        String challenge,
        String plot_public_key,
        Object pool_contract_puzzle_hash,
        String pool_public_key,
        String proof,
        long size
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.PROOF_OF_SPACE;
    }
}