package io.mindspice.schemas.wallet.did;

import com.fasterxml.jackson.annotation.JsonProperty;


public record DIDCoin(
        @JsonProperty("wallet_id") int walletId,
        @JsonProperty("did_innerpuz") String didInnerpuz,
        @JsonProperty("success") boolean success,
        @JsonProperty("did_parent") String didParent,
        @JsonProperty("did_amount") int didAmount,
        @JsonProperty("my_did") String myDid,
        @JsonProperty("error") String error
) { }