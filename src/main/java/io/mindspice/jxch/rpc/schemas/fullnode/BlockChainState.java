package io.mindspice.jxch.rpc.schemas.fullnode;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.jxch.rpc.schemas.object.Coin;
import io.mindspice.jxch.rpc.schemas.object.Output;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;


//FIXME Epoch Summary?
public record BlockChainState(
        @JsonProperty("node_id") String nodeId,
        @JsonProperty("difficulty") int difficulty,
        @JsonProperty("genesis_challenge_initialized") boolean genesisChallengeInitialized,
        @JsonProperty("mempool_size") long mempoolSize,
        @JsonProperty("mempool_cost") long mempoolCost,
        @JsonProperty("mempool_fees") long mempoolFees,
        @JsonProperty("mempool_min_fees") MemPoolFees mempoolMinFees,
        @JsonProperty("mempool_max_total_cost") long mempoolMaxTotalCost,
        @JsonProperty("block_max_cost") long blockMaxCost,
        @JsonProperty("peak") Peak peak,
        @JsonProperty("space") BigInteger space,
        @JsonProperty("sub_slot_iters") long subSlotIters,
        @JsonProperty("sync") Sync sync
) {
    public record Peak(
            @JsonProperty("challenge_block_info_hash") String challengeBlockInfoHash,
            @JsonProperty("challenge_vdf_output") Output challengeVdfOutput,
            @JsonProperty("deficit") int deficit,
            @JsonProperty("farmer_puzzle_hash") String farmerPuzzleHash,
            @JsonProperty("fees") long fees,
            @JsonProperty("finished_challenge_slot_hashes") List<String> finishedChallengeSlotHashes,
            @JsonProperty("finished_infused_challenge_slot_hashes") List<String> finishedInfusedChallengeSlotHashes,
            @JsonProperty("finished_reward_slot_hashes") List<String> finishedRewardSlotHashes,
            @JsonProperty("header_hash") String headerHash,
            @JsonProperty("height") int height,
            @JsonProperty("infused_challenge_vdf_output") Output infusedChallengeVdfOutput,
            @JsonProperty("overflow") boolean overflow,
            @JsonProperty("pool_puzzle_hash") String poolPuzzleHash,
            @JsonProperty("prev_hash") String prevHash,
            @JsonProperty("prev_transaction_block_hash") String prevTransactionBlockHash,
            @JsonProperty("prev_transaction_block_height") int prevTransactionBlockHeight,
            @JsonProperty("required_iters") long requiredIters,
            @JsonProperty("reward_claims_incorporated") List<Coin> rewardClaimsIncorporated,
            @JsonProperty("reward_infusion_new_challenge") String rewardInfusionNewChallenge,
            @JsonProperty("signage_point_index") int signagePointIndex,
            @JsonProperty("sub_epoch_summary_included") boolean subEpochSummaryIncluded,
            @JsonProperty("sub_slot_iters") long subSlotIters,
            @JsonProperty("timestamp") int timestamp,
            @JsonProperty("total_iters") long totalIters,
            @JsonProperty("weight") long weight

    ) {
        public Peak {
            finishedChallengeSlotHashes = finishedChallengeSlotHashes != null
                    ? Collections.unmodifiableList(finishedChallengeSlotHashes)
                    : List.of();

            finishedInfusedChallengeSlotHashes = finishedInfusedChallengeSlotHashes != null
                    ? Collections.unmodifiableList(finishedInfusedChallengeSlotHashes)
                    : List.of();

            finishedRewardSlotHashes = finishedRewardSlotHashes != null
                    ? Collections.unmodifiableList(finishedRewardSlotHashes)
                    : List.of();
            rewardClaimsIncorporated = rewardClaimsIncorporated != null
                    ? Collections.unmodifiableList(rewardClaimsIncorporated)
                    : List.of();
        }
    }


    public record Sync(
            @JsonProperty("sync_mode") boolean syncMode,
            @JsonProperty("sync_progress_height") int syncProgressHeight,
            @JsonProperty("sync_tip_height") int syncTipHeight,
            @JsonProperty("synced") boolean synced
    ) { }


    public record MemPoolFees(long cost_5000000) { }
}


