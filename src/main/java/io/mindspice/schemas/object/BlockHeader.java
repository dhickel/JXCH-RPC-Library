package io.mindspice.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;


public record BlockHeader(
        @JsonProperty("finished_sub_slots") List<SubSlot> finishedSubSlots,
        @JsonProperty("reward_chain_block") RewardChainBlock rewardChainBlock,
        @JsonProperty("challenge_chain_sp_proof") Proof challengeChainSpProof,
        @JsonProperty("reward_chain_sp_proof") Proof rewardChainSpProof,
        @JsonProperty("foliage") Foliage foliage,
        @JsonProperty("foliage_transaction_block") FoliageTransactionBlock foliageTransactionBlock,
        @JsonProperty("transactions_filter") String transactionsFilter
) {
    public BlockHeader {
        finishedSubSlots = finishedSubSlots != null ? Collections.unmodifiableList(finishedSubSlots) : List.of();
    }
}