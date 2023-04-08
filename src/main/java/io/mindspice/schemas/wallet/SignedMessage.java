package io.mindspice.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SignedMessage(
	@JsonProperty("signature") String signature,
	@JsonProperty("signing_mode") String signingMode,
	@JsonProperty("latest_coin_id") String latestCoinId,
	@JsonProperty("success") boolean success,
	@JsonProperty("error") String error,
	@JsonProperty("pubkey") String pubkey
) {
}