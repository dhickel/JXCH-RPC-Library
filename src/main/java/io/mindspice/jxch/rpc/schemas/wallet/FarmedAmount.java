package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;

public record FarmedAmount(
	@JsonProperty("farmed_amount") BigInteger farmedAmount,
	@JsonProperty("fee_amount") BigInteger feeAmount,
	@JsonProperty("farmer_reward_amount") BigInteger farmerRewardAmount,
	@JsonProperty("pool_reward_amount") BigInteger poolRewardAmount,
	@JsonProperty("success") boolean success,
	@JsonProperty("last_height_farmed") long lastHeightFarmed,
	@JsonProperty("error") String error,
	@JsonProperty("last_time_farmed") BigInteger lastTimeFarmed,
	@JsonProperty("blocks_won") long blocksWon
) {
	public FarmedAmount(long farmedAmount, long feeAmount, long farmerRewardAmount, long poolRewardAmount,
			boolean success, int lastHeightFarmed, String error) {
		this(BigInteger.valueOf(farmedAmount), BigInteger.valueOf(feeAmount), BigInteger.valueOf(farmerRewardAmount),
				BigInteger.valueOf(poolRewardAmount), success, lastHeightFarmed, error, BigInteger.ZERO, 0);
	}
}