package io.mindspice.jxch.rpc.schemas.wallet.offers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import io.mindspice.jxch.rpc.util.JsonUtils;

import java.util.Collections;
import java.util.Map;
import java.util.List;
import java.math.BigInteger;
import java.util.stream.Collectors;
import io.mindspice.jxch.rpc.schemas.wallet.ValidTimes;


public record OfferSummary(
	@JsonProperty("fees") BigInteger fees,
	@JsonProperty("requested") Map<String, String> requested,
	@JsonProperty("offered") Map<String, String> offered,
	@JsonProperty("infos") JsonNode infos,
	@JsonProperty("additions") List<String> additions,
	@JsonProperty("removals") List<String> removals,
	@JsonProperty("valid_times") ValidTimes validTimes
) {
	public OfferSummary(long fees, Map<String, Long> requested, Map<String, Long> offered, JsonNode infos) {
		this(BigInteger.valueOf(fees),
				requested == null ? Map.of() : requested.entrySet().stream()
						.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString())),
				offered == null ? Map.of() : offered.entrySet().stream()
						.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString())),
				infos, List.of(), List.of(), null);
	}

	public OfferSummary {
		additions = additions != null ? List.copyOf(additions) : List.of();
		removals = removals != null ? List.copyOf(removals) : List.of();
		requested = requested !=null ? Collections.unmodifiableMap(requested) : Map.of();
		offered = offered != null ? Collections.unmodifiableMap(offered) : Map.of();
		if (infos == null) infos = JsonUtils.newEmptyNode();
	}
}