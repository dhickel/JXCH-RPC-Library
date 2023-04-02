package io.mindspice.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;


public record FoliageTransactionBlock(
        @JsonProperty("prev_transaction_block_hash") String prevTransactionBlockHash,
        @JsonProperty("timestamp") long timestamp,
        @JsonProperty("filter_hash") String filterHash,
        @JsonProperty("additions_root") String additionsRoot,
        @JsonProperty("removals_root") String removalsRoot,
        @JsonProperty("transactions_info_hash") String transactionsInfoHash
) { }