package io.mindspice.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CatAssetInfo(
	@JsonProperty("wallet_id") int walletId,
	@JsonProperty("success") boolean success,
	@JsonProperty("name") String name,
	@JsonProperty("error") String error
) {
}