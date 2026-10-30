package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;


public record FoliageTransactionBlock(
        @JsonProperty("prev_transaction_block_hash") String prevTransactionBlockHash,
        @JsonProperty("timestamp") BigInteger timestamp,
        @JsonProperty("filter_hash") String filterHash,
        @JsonProperty("additions_root") String additionsRoot,
        @JsonProperty("removals_root") String removalsRoot,
        @JsonProperty("transactions_info_hash") String transactionsInfoHash
) {
    public FoliageTransactionBlock(String prevTransactionBlockHash, long timestamp, String filterHash,
                                   String additionsRoot, String removalsRoot, String transactionsInfoHash) {
        this(prevTransactionBlockHash, BigInteger.valueOf(timestamp), filterHash, additionsRoot,
                removalsRoot, transactionsInfoHash);
    }
}