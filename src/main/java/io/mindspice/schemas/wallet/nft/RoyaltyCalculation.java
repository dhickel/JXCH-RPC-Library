package io.mindspice.schemas.wallet.nft;

import com.fasterxml.jackson.annotation.JsonProperty;


public record RoyaltyCalculation(
        @JsonProperty("address") String address,
        @JsonProperty("amount") long amount,
        @JsonProperty("asset") String asset
) { }
