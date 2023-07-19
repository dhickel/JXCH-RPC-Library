package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Notification(
	@JsonProperty("amount") long amount,
	@JsonProperty("id") String id,
	@JsonProperty("message") String message,
	@JsonProperty("height") int height
) {
}