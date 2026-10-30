package io.mindspice.jxch.rpc.schemas.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RewardTarget(
	@JsonProperty("have_farmer_sk") Boolean haveFarmerSk,
	@JsonProperty("success") boolean success,
	@JsonProperty("farmer_target") String farmerTarget,
	@JsonProperty("pool_target") String poolTarget,
	@JsonProperty("have_pool_sk") Boolean havePoolSk,
	@JsonProperty("error") String error
) {
}