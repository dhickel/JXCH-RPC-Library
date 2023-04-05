package io.mindspice.schemas.wallet.did;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public record NewWallet(
        @JsonAlias({"asset_id", "my_did",}) String info,
        @JsonProperty("type") int type,
        @JsonProperty("wallet_id") int walletId,
        @JsonProperty("success") boolean success
){}




