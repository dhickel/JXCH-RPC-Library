package io.mindspice.schemas.wallet.did;

import com.fasterxml.jackson.annotation.JsonProperty;


public record DID(
        @JsonProperty("wallet_id") int walletId,
        @JsonProperty("coin_id") String coinId,
        @JsonProperty("success") boolean success,
        @JsonProperty("my_did") String myDid,
        @JsonProperty("error") String error
) {
}