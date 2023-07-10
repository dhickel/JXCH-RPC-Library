package io.mindspice.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;


public record FoliageBlockData(
        @JsonProperty("extension_data") String extensionData,
        @JsonProperty("farmer_reward_puzzle_hash") String farmerRewardPuzzleHash,
        @JsonProperty("pool_signature") String poolSignature,
        @JsonProperty("pool_target") PoolTarget poolTarget,
        @JsonProperty("unfinished_reward_block_hash") String unfinishedRewardBlockHash
) { }