package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public record BlockHeader(
        @JsonProperty("finished_sub_slots") List<SubSlot> finishedSubSlots,
        @JsonProperty("reward_chain_block") RewardChainBlockUnfinished rewardChainBlock,
        @JsonProperty("challenge_chain_sp_proof") Proof challengeChainSpProof,
        @JsonProperty("reward_chain_sp_proof") Proof rewardChainSpProof,
        @JsonProperty("foliage") Foliage foliage,
        @JsonProperty("foliage_transaction_block") FoliageTransactionBlock foliageTransactionBlock,
        @JsonProperty("transactions_filter") String transactionsFilter
) {
    public BlockHeader {
        finishedSubSlots = finishedSubSlots == null ? List.of() : List.copyOf(finishedSubSlots);
    }

    @Deprecated
    public BlockHeader(List<SubSlot> finishedSubSlots, Object rewardChainBlock,
                       Proof challengeChainSpProof, Proof rewardChainSpProof, Foliage foliage,
                       FoliageTransactionBlock foliageTransactionBlock, String transactionsFilter) {
        this(finishedSubSlots, unfinished(rewardChainBlock), challengeChainSpProof, rewardChainSpProof,
                foliage, foliageTransactionBlock, transactionsFilter);
    }

    private static RewardChainBlockUnfinished unfinished(Object value) {
        if (value == null) {
            return null;
        }
        RewardChainBlock block = (RewardChainBlock) value;
        return new RewardChainBlockUnfinished(block.totalIters(), block.signagePointIndex(),
                block.posSsCcChallengeHash(), block.proofOfSpace(), block.challengeChainSpVdf(),
                block.challengeChainSpSignature(), block.rewardChainSpVdf(), block.rewardChainSpSignature());
    }
}