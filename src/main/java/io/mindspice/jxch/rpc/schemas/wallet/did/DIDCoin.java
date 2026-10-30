package io.mindspice.jxch.rpc.schemas.wallet.did;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;


public record DIDCoin(
        @JsonProperty("wallet_id") long walletId,
        @JsonProperty("did_innerpuz") String didInnerpuz,
        @JsonProperty("success") boolean success,
        @JsonProperty("did_parent") String didParent,
        @JsonProperty("did_amount") BigInteger didAmount,
        @JsonProperty("my_did") String myDid,
        @JsonProperty("error") String error
) {
    public DIDCoin(int walletId, String didInnerpuz, boolean success, String didParent,
            long didAmount, String myDid, String error) {
        this(walletId, didInnerpuz, success, didParent, BigInteger.valueOf(didAmount), myDid, error);
    }
}