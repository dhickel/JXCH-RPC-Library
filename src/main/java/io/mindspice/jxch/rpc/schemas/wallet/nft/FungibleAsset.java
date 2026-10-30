package io.mindspice.jxch.rpc.schemas.wallet.nft;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;


public record FungibleAsset(
        @JsonProperty("asset") String asset,
        @JsonProperty("amount") BigInteger amount
) {
    public FungibleAsset(String asset, long amount) {
        this(asset, BigInteger.valueOf(amount));
    }
}
