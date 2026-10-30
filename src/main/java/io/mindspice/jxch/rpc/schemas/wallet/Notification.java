package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;

public record Notification(
	@JsonProperty("amount") BigInteger amount,
	@JsonProperty("id") String id,
	@JsonProperty("message") String message,
	@JsonProperty("height") long height
) {
    public Notification(long amount, String id, String message, int height) {
        this(BigInteger.valueOf(amount), id, message, height);
    }
}