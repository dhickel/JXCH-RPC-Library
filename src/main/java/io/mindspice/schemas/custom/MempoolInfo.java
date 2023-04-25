package io.mindspice.schemas.custom;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MempoolInfo(

	@JsonProperty("mempool_current_cost_size")
	String mempoolCurrentCostSize,

	@JsonProperty("mempool_max_cost_size")
	String mempoolMaxCostSize
) {
}