package io.mindspice.schemas.components;

public record BlockSpend(
        Coin coin,
        String puzzle_reveal,
        String solution) { }
