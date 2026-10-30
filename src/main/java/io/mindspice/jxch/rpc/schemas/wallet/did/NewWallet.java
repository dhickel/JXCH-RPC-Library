package io.mindspice.jxch.rpc.schemas.wallet.did;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public record NewWallet(
        @JsonAlias({"asset_id", "my_did",}) String info,
        @JsonProperty("type") String type,
        @JsonProperty("wallet_id") long walletId,
        @JsonProperty("success") boolean success
) {
    public NewWallet(String info, int type, int walletId, boolean success) {
        this(info, io.mindspice.jxch.rpc.enums.WalletType.fromValue(type).name(), walletId, success);
    }
}




