package io.mindspice.jxch.rpc.schemas.fullnode;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.List;

public record FeeEstimate(
        @JsonProperty("mempool_fees") BigInteger mempoolFees,
        @JsonProperty("target_times") List<BigInteger> targetTimes,
        @JsonProperty("full_node_synced") boolean fullNodeSynced,
        @JsonProperty("last_tx_block_height") long lastTxBlockHeight,
        @JsonProperty("mempool_size") BigInteger mempoolSize,
        @JsonProperty("fee_rate_last_block") double feeRateLastBlock,
        @JsonProperty("error") String error,
        @JsonProperty("num_spends") long numSpends,
        @JsonProperty("last_block_cost") BigInteger lastBlockCost,
        @JsonProperty("current_fee_rate") double currentFeeRate,
        @JsonProperty("fees_last_block") BigInteger feesLastBlock,
        @JsonProperty("node_time_utc") BigInteger nodeTimeUtc,
        @JsonProperty("success") boolean success,
        @JsonProperty("last_peak_timestamp") BigInteger lastPeakTimestamp,
        @JsonProperty("mempool_max_size") BigInteger mempoolMaxSize,
        @JsonProperty("peak_height") long peakHeight,
        @JsonProperty("estimates") List<BigInteger> estimates
) {
    public FeeEstimate {
        targetTimes = targetTimes == null ? List.of() : List.copyOf(targetTimes);
        estimates = estimates == null ? List.of() : List.copyOf(estimates);
    }

    @Deprecated
    public FeeEstimate(long mempoolFees, List<Integer> targetTimes, boolean fullNodeSynced, int lastTxBlockHeight,
                       long mempoolSize, long feeRateLastBlock, String error, int numSpends, long lastBlockCost,
                       long currentFeeRate, long feesLastBlock, int nodeTimeUtc, boolean success,
                       int lastPeakTimestamp, long mempoolMaxSize, int peakHeight, List<Integer> estimates) {
        this(BigInteger.valueOf(mempoolFees),
                targetTimes == null ? List.of() : targetTimes.stream().map(t -> BigInteger.valueOf(t)).toList(),
                fullNodeSynced, lastTxBlockHeight,
                BigInteger.valueOf(mempoolSize), feeRateLastBlock, error, numSpends, BigInteger.valueOf(lastBlockCost),
                currentFeeRate, BigInteger.valueOf(feesLastBlock), BigInteger.valueOf(nodeTimeUtc), success,
                BigInteger.valueOf(lastPeakTimestamp), BigInteger.valueOf(mempoolMaxSize), peakHeight,
                estimates == null ? List.of() : estimates.stream().map(e -> BigInteger.valueOf(e)).toList());
    }
}
