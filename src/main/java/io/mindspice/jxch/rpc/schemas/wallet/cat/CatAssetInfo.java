package io.mindspice.jxch.rpc.schemas.wallet.cat;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CatAssetInfo(
	@JsonProperty("wallet_id") Long walletId,
	@JsonProperty("success") boolean success,
	@JsonProperty("name") String name,
	@JsonProperty("error") String error
) {
	public CatAssetInfo(int walletId, boolean success, String name, String error) {
		this(Long.valueOf(walletId), success, name, error);
	}
}