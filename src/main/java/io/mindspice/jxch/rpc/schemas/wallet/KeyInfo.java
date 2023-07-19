package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;


public record KeyInfo(
        @JsonProperty("success") boolean success,
        @JsonProperty("error") String error,
        @JsonProperty("fingerprint") long fingerprint,
        @JsonProperty("used_for_pool_rewards") boolean usedForPoolRewards,
        @JsonProperty("wallet_balance") boolean walletBalance,
        @JsonProperty("used_for_farmer_rewards") boolean usedForFarmerRewards
) { }