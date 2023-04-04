package io.mindspice.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Cat(
	@JsonProperty("symbol") String symbol,
	@JsonProperty("name") String name,
	@JsonProperty("asset_id") String assetId
) {
}