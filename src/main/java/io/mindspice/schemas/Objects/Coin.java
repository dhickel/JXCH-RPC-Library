package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;


public record Coin (
        String parent_coin_info,
        String puzzle_hash,
        long amount
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.COIN;
    }
}