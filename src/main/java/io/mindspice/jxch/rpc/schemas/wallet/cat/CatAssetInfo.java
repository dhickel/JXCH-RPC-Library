package io.mindspice.jxch.rpc.schemas.wallet.cat;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CatAssetInfo(
	@JsonProperty("wallet_id") int walletId,
	@JsonProperty("success") boolean success,
	@JsonProperty("name") String name,
	@JsonProperty("error") String error
) {
}