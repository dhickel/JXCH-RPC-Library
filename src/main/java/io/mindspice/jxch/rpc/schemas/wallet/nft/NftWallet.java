package io.mindspice.jxch.rpc.schemas.wallet.nft;

import com.fasterxml.jackson.annotation.JsonProperty;


public record NftWallet(
        @JsonProperty("did_id") String didId,
        @JsonProperty("wallet_id") int walletId,
        @JsonProperty("did_wallet_id") int didWalletId
) { }