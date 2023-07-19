package io.mindspice.jxch.rpc.schemas.fullnode;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;


public record FeeEstimate(
        @JsonProperty("mempool_fees") long mempoolFees,
        @JsonProperty("target_times") List<Integer> targetTimes,
        @JsonProperty("full_node_synced") boolean fullNodeSynced,
        @JsonProperty("last_tx_block_height") int lastTxBlockHeight,
        @JsonProperty("mempool_size") long mempoolSize,
        @JsonProperty("fee_rate_last_block") long feeRateLastBlock,
        @JsonProperty("error") String error,
        @JsonProperty("num_spends") int numSpends,
        @JsonProperty("last_block_cost") long lastBlockCost,
        @JsonProperty("current_fee_rate") long currentFeeRate,
        @JsonProperty("fees_last_block") long feesLastBlock,
        @JsonProperty("node_time_utc") int nodeTimeUtc,
        @JsonProperty("success") boolean success,
        @JsonProperty("last_peak_timestamp") int lastPeakTimestamp,
        @JsonProperty("mempool_max_size") long mempoolMaxSize,
        @JsonProperty("peak_height") int peakHeight,
        @JsonProperty("estimates") List<Integer> estimates

) {
    public FeeEstimate {
        targetTimes = targetTimes != null ? Collections.unmodifiableList(targetTimes) : List.of();
    }
}