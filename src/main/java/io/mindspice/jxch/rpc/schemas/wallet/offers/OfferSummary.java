package io.mindspice.jxch.rpc.schemas.wallet.offers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.util.JsonUtils;

import java.util.Collections;
import java.util.Map;


public record OfferSummary(
	@JsonProperty("fees") int fees,
	@JsonProperty("requested") Map<String, Long> requested,
	@JsonProperty("offered") Map<String, Long> offered,
	@JsonProperty("infos") JsonNode infos
) {
	public OfferSummary {
		requested = requested !=null ? Collections.unmodifiableMap(requested) : Map.of();
		offered = offered != null ? Collections.unmodifiableMap(offered) : Map.of();
		if (infos == null) infos = JsonUtils.newEmptyNode();
	}
}