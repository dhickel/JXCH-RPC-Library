package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;


public record CoinSpend(
        Coin coin,
        String puzzle_reveal,
        String solution
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.COIN_SPEND;
    }
}