package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CoinAnnouncement(
	@JsonProperty("morph_bytes") String morphBytes,
	@JsonProperty("coin_id") String coinId,
	@JsonProperty("message") String message
) {
}