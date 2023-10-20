package io.mindspice.jxch.rpc.enums;

public enum WalletType {

    STANDARD_WALLET(0),
    ATOMIC_SWAP(2),
    AUTHORIZED_PAYEE(3),
    MULTI_SIG(4),
    CUSTODY(5),
    CAT(6),
    RECOVERABLE(7),
    DISTRIBUTED_ID(8),
    POOLING_WALLET(9),
    NFT(10),
    DATA_LAYER(11),
    DATA_LAYER_OFFER(12),
    VC(13),
    CRCAT(14);

    public final int value;

    WalletType(int value) {
        this.value = value;
    }
}
