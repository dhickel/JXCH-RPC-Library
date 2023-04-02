package io.mindspice.schemas.object;


import com.fasterxml.jackson.annotation.JsonProperty;


public record Foliage(
        @JsonProperty("foliage_block_data") FoliageBlockData foliageBlockData,
        @JsonProperty("foliage_block_data_signature") String foliageBlockDataSignature,
        @JsonProperty("foliage_transaction_block_signature") String foliageTransactionBlockSignature,
        @JsonProperty("foliage_transaction_block_hash") String foliageTransactionBlockHash,
        @JsonProperty("prev_block_hash") String prevBlockHash,
        @JsonProperty("reward_block_hash") String rewardBlockHash
) { }