package io.mindspice.schemas.components;

public record Coin (
        String parent_coin_info,
        String puzzle_hash,
        long amount
) {
}