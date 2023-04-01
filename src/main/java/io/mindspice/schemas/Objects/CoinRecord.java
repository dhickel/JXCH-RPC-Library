package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;


public record CoinRecord(
        Coin coin,
        int confirmed_block_index,
        int spent_block_index,
        boolean spent,
        boolean coinbase,
        long timestamp
)  implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.COIN_RECORD;
    }
}

