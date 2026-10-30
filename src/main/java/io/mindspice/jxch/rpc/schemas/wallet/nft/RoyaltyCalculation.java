package io.mindspice.jxch.rpc.schemas.wallet.nft;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;


public record RoyaltyCalculation(
        @JsonProperty("address") String address,
        @JsonProperty("amount") BigInteger amount,
        @JsonProperty("asset") String asset
) {
    public RoyaltyCalculation(String address, long amount, String asset) {
        this(address, BigInteger.valueOf(amount), asset);
    }
}
