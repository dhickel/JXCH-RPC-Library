package io.mindspice.jxch.rpc.enums;

import java.util.Arrays;


public enum CoinType {
    NORMAL(0),
    CLAWBACK(1),
    CRCAT_PENDING(2),
    CRCAT(3);

    public final int enumValue;

    CoinType(int value) {
        this.enumValue = value;
    }

    public static CoinType fromEnumValue(int value) {
        return Arrays.stream(CoinType.values())
                .filter(wt -> wt.enumValue == value).findFirst()
                .orElseThrow(() ->new IllegalStateException("Unknown Coin Type"));
    }
}
