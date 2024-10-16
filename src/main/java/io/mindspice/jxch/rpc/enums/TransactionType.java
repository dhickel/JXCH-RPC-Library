package io.mindspice.jxch.rpc.enums;

import java.util.Arrays;


public enum TransactionType {
    INCOMING_TX(0),
    OUTGOING_TX(1),
    COINBASE_REWARD(2),
    FEE_REWARD(3),
    INCOMING_TRADE(4),
    OUTGOING_TRADE(5);

    public final int value;

    TransactionType(int value) {
        this.value = value;
    }

    public static CoinType fromEnumValue(int value) {
        return Arrays.stream(CoinType.values())
                .filter(wt -> wt.enumValue == value).findFirst()
                .orElseThrow(() -> new IllegalStateException("Unknown Coin Type"));
    }
}
