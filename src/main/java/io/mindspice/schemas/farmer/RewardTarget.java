package io.mindspice.schemas.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RewardTarget(
	@JsonProperty("have_farmer_sk") boolean haveFarmerSk,
	@JsonProperty("success") boolean success,
	@JsonProperty("farmer_target") String farmerTarget,
	@JsonProperty("pool_target") String poolTarget,
	@JsonProperty("have_pool_sk") boolean havePoolSk,
	@JsonProperty("error") String error
) {
}