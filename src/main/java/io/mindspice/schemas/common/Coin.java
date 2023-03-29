package io.mindspice.schemas.common;

public record Coin(
        String parent_coin_info,
        String puzzle_hash,
        long amount
) { }