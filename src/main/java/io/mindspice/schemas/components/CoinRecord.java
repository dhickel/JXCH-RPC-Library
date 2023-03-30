package io.mindspice.schemas.components;

public record CoinRecord(
        Coin coin,
        int confirmed_block_index,
        int spent_block_index,
        boolean spent,
        boolean coinbase,
        long timestamp
)  {

}

