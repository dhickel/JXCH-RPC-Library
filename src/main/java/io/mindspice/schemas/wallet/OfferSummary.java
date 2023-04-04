package io.mindspice.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OfferSummary(

	@JsonProperty("fees") int fees,
	@JsonProperty("requested") Requested requested,
	@JsonProperty("offered") Offered offered,
	@JsonProperty("infos") Infos infos
) {
}