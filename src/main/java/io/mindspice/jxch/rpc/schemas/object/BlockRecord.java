package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.List;

public record BlockRecord(
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
    public BlockRecord {
        finishedChallengeSlotHashes = finishedChallengeSlotHashes == null ? null : List.copyOf(finishedChallengeSlotHashes);
        finishedInfusedChallengeSlotHashes = finishedInfusedChallengeSlotHashes == null ? null : List.copyOf(finishedInfusedChallengeSlotHashes);
        finishedRewardSlotHashes = finishedRewardSlotHashes == null ? null : List.copyOf(finishedRewardSlotHashes);
        rewardClaimsIncorporated = rewardClaimsIncorporated == null ? null : List.copyOf(rewardClaimsIncorporated);
    }

    @Deprecated
    public BlockRecord(String challengeBlockInfoHash, Output challengeVdfOutput, int deficit,
                       String farmerPuzzleHash, long fees, List<String> finishedChallengeSlotHashes,
                       List<String> finishedInfusedChallengeSlotHashes, List<String> finishedRewardSlotHashes,
                       String headerHash, int height, Output infusedChallengeVdfOutput, boolean overflow,
                       String poolPuzzleHash, String prevHash, String prevTransactionBlockHash,
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
