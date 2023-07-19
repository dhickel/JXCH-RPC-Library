package io.mindspice.jxch.rpc.schemas.wallet.cat;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Cat(
	@JsonProperty("symbol") String symbol,
	@JsonProperty("name") String name,
	@JsonProperty("asset_id") String assetId
) {
}