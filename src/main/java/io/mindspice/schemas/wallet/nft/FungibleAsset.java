package io.mindspice.schemas.wallet.nft;

import com.fasterxml.jackson.annotation.JsonProperty;


public record FungibleAsset(
        @JsonProperty("asset") String asset,
        @JsonProperty("amount") long amount
) {
}
