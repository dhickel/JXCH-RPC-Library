package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FarmedAmount(
	@JsonProperty("farmed_amount") long farmedAmount,
	@JsonProperty("fee_amount") long feeAmount,
	@JsonProperty("farmer_reward_amount") long farmerRewardAmount,
	@JsonProperty("pool_reward_amount") long poolRewardAmount,
	@JsonProperty("success") boolean success,
	@JsonProperty("last_height_farmed") int lastHeightFarmed,
	@JsonProperty("error") String error
) {
}