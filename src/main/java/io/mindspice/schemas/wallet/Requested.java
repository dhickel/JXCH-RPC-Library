package io.mindspice.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Requested(

	@JsonProperty("xch")
	long xch
) {
}