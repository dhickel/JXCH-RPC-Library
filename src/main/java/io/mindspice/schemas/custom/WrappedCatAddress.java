package io.mindspice.schemas.custom;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WrappedCatAddress(

	@JsonProperty("wrapped_address")
	String wrappedAddress,

	@JsonProperty("wrapped_puzzle_hash")
	String wrappedPuzzleHash
) {
}