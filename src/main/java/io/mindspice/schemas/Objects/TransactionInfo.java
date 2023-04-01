package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;

import java.util.Collections;
import java.util.List;


public record TransactionInfo(
        String generator_root,
        String generator_refs_root,
        String aggregated_signature,
        String fees,
        String cost,
        List<Coin> reward_claims_incorporated
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.TRANSACTION_INFO;
    }


    public TransactionInfo {
        reward_claims_incorporated = reward_claims_incorporated == null
                ? List.of()
                : Collections.unmodifiableList(reward_claims_incorporated);
    }
}

