package io.mindspice.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;


public record BlockRecord(
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
    public BlockRecord {
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








