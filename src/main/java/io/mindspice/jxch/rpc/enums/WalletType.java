package io.mindspice.jxch.rpc.enums;

import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;


public enum WalletType {
    STANDARD_WALLET(0),
    ATOMIC_SWAP(2),
    AUTHORIZED_PAYEE(3),
    MULTI_SIG(4),
    CUSTODY(5),
    CAT(6),
    RECOVERABLE(7),
    DECENTRALIZED_ID(8),
    @Deprecated
    DISTRIBUTED_ID(8),
    POOLING_WALLET(9),
    NFT(10),
    DATA_LAYER(11),
    DATA_LAYER_OFFER(12),
    VC(13),
    CRCAT(57),
    RCAT(132),
    REMOTE(205),
    PLOTNFT_2(209);

    public final int enumValue;

    WalletType(int value) {
        this.enumValue = value;
    }

    @JsonValue
    public int value() {
        return enumValue;
    }

    @JsonCreator
    public static WalletType fromValue(int value) {
        return Arrays.stream(WalletType.values())
                .filter(wt -> wt.enumValue == value).findFirst()
                .orElseThrow(() ->new IllegalStateException("Unknown Wallet Type"));
    }
}
