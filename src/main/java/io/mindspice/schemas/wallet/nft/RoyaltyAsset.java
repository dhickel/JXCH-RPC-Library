package io.mindspice.schemas.wallet.nft;

import com.fasterxml.jackson.annotation.JsonProperty;


public record RoyaltyAsset(
        @JsonProperty("asset") String asset,
        @JsonProperty("royalty_address") String royaltyAddress,
        @JsonProperty("royalty_percentage") int royaltyPercentage
) { }
