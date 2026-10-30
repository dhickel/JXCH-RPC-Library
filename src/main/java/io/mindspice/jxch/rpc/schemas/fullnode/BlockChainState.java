package io.mindspice.jxch.rpc.schemas.fullnode;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.jxch.rpc.schemas.object.Coin;
import io.mindspice.jxch.rpc.schemas.object.Output;
import io.mindspice.jxch.rpc.schemas.object.SubEpochSummary;

import java.math.BigInteger;
import java.util.List;

public record BlockChainState(
        @JsonProperty("node_id") String nodeId,
        @JsonProperty("difficulty") BigInteger difficulty,
        @JsonProperty("genesis_challenge_initialized") boolean genesisChallengeInitialized,
        @JsonProperty("mempool_size") long mempoolSize,
        @JsonProperty("mempool_cost") BigInteger mempoolCost,
        @JsonProperty("mempool_fees") BigInteger mempoolFees,
        @JsonProperty("mempool_min_fees") MemPoolFees mempoolMinFees,
        @JsonProperty("mempool_max_total_cost") BigInteger mempoolMaxTotalCost,
        @JsonProperty("block_max_cost") BigInteger blockMaxCost,
        @JsonProperty("peak") Peak peak,
        @JsonProperty("space") BigInteger space,
        @JsonProperty("sub_slot_iters") BigInteger subSlotIters,
        @JsonProperty("sync") Sync sync,
        @JsonProperty("average_block_time") Double averageBlockTime
) {
    @Deprecated
    public BlockChainState(String nodeId, int difficulty, boolean genesisChallengeInitialized, long mempoolSize,
                           long mempoolCost, long mempoolFees, MemPoolFees mempoolMinFees, long mempoolMaxTotalCost,
                           long blockMaxCost, Peak peak, BigInteger space, long subSlotIters, Sync sync) {
        this(nodeId, BigInteger.valueOf(difficulty), genesisChallengeInitialized, mempoolSize,
                BigInteger.valueOf(mempoolCost), BigInteger.valueOf(mempoolFees), mempoolMinFees,
                BigInteger.valueOf(mempoolMaxTotalCost), BigInteger.valueOf(blockMaxCost), peak, space,
                BigInteger.valueOf(subSlotIters), sync, null);
    }

    public record Peak(
            @JsonProperty("challenge_block_info_hash") String challengeBlockInfoHash,
            @JsonProperty("challenge_vdf_output") Output challengeVdfOutput,
            @JsonProperty("deficit") int deficit,
            @JsonProperty("farmer_puzzle_hash") String farmerPuzzleHash,
            @JsonProperty("fees") BigInteger fees,
            @JsonProperty("finished_challenge_slot_hashes") List<String> finishedChallengeSlotHashes,
            @JsonProperty("finished_infused_challenge_slot_hashes") List<String> finishedInfusedChallengeSlotHashes,
            @JsonProperty("finished_reward_slot_hashes") List<String> finishedRewardSlotHashes,
            @JsonProperty("header_hash") String headerHash,
            @JsonProperty("height") long height,
            @JsonProperty("infused_challenge_vdf_output") Output infusedChallengeVdfOutput,
            @JsonProperty("overflow") boolean overflow,
            @JsonProperty("pool_puzzle_hash") String poolPuzzleHash,
            @JsonProperty("prev_hash") String prevHash,
            @JsonProperty("prev_transaction_block_hash") String prevTransactionBlockHash,
            @JsonProperty("prev_transaction_block_height") long prevTransactionBlockHeight,
            @JsonProperty("required_iters") BigInteger requiredIters,
            @JsonProperty("reward_claims_incorporated") List<Coin> rewardClaimsIncorporated,
            @JsonProperty("reward_infusion_new_challenge") String rewardInfusionNewChallenge,
            @JsonProperty("signage_point_index") int signagePointIndex,
            @JsonProperty("sub_epoch_summary_included") SubEpochSummary subEpochSummaryIncluded,
            @JsonProperty("sub_slot_iters") BigInteger subSlotIters,
            @JsonProperty("timestamp") BigInteger timestamp,
            @JsonProperty("total_iters") BigInteger totalIters,
            @JsonProperty("weight") BigInteger weight
    ) {
        public Peak {
            finishedChallengeSlotHashes = finishedChallengeSlotHashes == null ? null : List.copyOf(finishedChallengeSlotHashes);
            finishedInfusedChallengeSlotHashes = finishedInfusedChallengeSlotHashes == null ? null : List.copyOf(finishedInfusedChallengeSlotHashes);
            finishedRewardSlotHashes = finishedRewardSlotHashes == null ? null : List.copyOf(finishedRewardSlotHashes);
            rewardClaimsIncorporated = rewardClaimsIncorporated == null ? null : List.copyOf(rewardClaimsIncorporated);
        }

        @Deprecated
        public Peak(String challengeBlockInfoHash, Output challengeVdfOutput, int deficit, String farmerPuzzleHash,
                    long fees, List<String> finishedChallengeSlotHashes, List<String> finishedInfusedChallengeSlotHashes,
                    List<String> finishedRewardSlotHashes, String headerHash, int height, Output infusedChallengeVdfOutput,
                    boolean overflow, String poolPuzzleHash, String prevHash, String prevTransactionBlockHash,
                    int prevTransactionBlockHeight, long requiredIters, List<Coin> rewardClaimsIncorporated,
                    String rewardInfusionNewChallenge, int signagePointIndex, boolean subEpochSummaryIncluded,
                    long subSlotIters, int timestamp, long totalIters, long weight) {
            this(challengeBlockInfoHash, challengeVdfOutput, deficit, farmerPuzzleHash, BigInteger.valueOf(fees),
                    finishedChallengeSlotHashes, finishedInfusedChallengeSlotHashes, finishedRewardSlotHashes,
                    headerHash, height, infusedChallengeVdfOutput, overflow, poolPuzzleHash, prevHash,
                    prevTransactionBlockHash, prevTransactionBlockHeight, BigInteger.valueOf(requiredIters),
                    rewardClaimsIncorporated, rewardInfusionNewChallenge, signagePointIndex,
                    noSummary(subEpochSummaryIncluded), BigInteger.valueOf(subSlotIters), BigInteger.valueOf(timestamp),
                    BigInteger.valueOf(totalIters), BigInteger.valueOf(weight));
        }

        private static SubEpochSummary noSummary(boolean included) {
            if (included) {
                throw new IllegalArgumentException("A sub-epoch summary requires SubEpochSummary data, not a boolean");
            }
            return null;
        }
    }

    public record Sync(
            @JsonProperty("sync_mode") boolean syncMode,
            @JsonProperty("sync_progress_height") long syncProgressHeight,
            @JsonProperty("sync_tip_height") long syncTipHeight,
            @JsonProperty("synced") boolean synced
    ) { }

    public record MemPoolFees(@JsonProperty("cost_5000000") double cost_5000000) { }
}
