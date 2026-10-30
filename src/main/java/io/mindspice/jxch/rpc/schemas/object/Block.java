package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Block(
        @JsonProperty("header_hash") String headerHash,
        @JsonProperty("challenge_chain_ip_proof") Proof challengeChainIpProof,
        @JsonProperty("challenge_chain_sp_proof") Proof challengeChainSpProof,
        @JsonProperty("finished_sub_slots") List<SubSlot> finishedSubSlots,
        @JsonProperty("foliage") Foliage foliage,
        @JsonProperty("foliage_transaction_block") FoliageTransactionBlock foliageTransactionBlock,
        @JsonProperty("infused_challenge_chain_ip_proof") Proof infusedChallengeChainIpProof,
        @JsonProperty("reward_chain_block") RewardChainBlock rewardChainBlock,
        @JsonProperty("reward_chain_ip_proof") Proof rewardChainIpProof,
        @JsonProperty("reward_chain_sp_proof") Proof rewardChainSpProof,
        @JsonProperty("transactions_generator") String transactionsGenerator,
        @JsonProperty("transactions_generator_ref_list") List<Long> transactionsGeneratorRefList,
        @JsonProperty("transactions_info") TransactionInfo transactionsInfo,
        @JsonProperty("transactions_generator_buffer") String transactionsGeneratorBuffer,
        @JsonProperty("version") int version
) {
    public Block {
        finishedSubSlots = finishedSubSlots == null ? List.of() : List.copyOf(finishedSubSlots);
        transactionsGeneratorRefList = transactionsGeneratorRefList == null ? List.of() : List.copyOf(transactionsGeneratorRefList);
    }

    @Deprecated
    public Block(String headerHash, Proof challengeChainIpProof, Proof challengeChainSpProof,
                 List<SubSlot> finishedSubSlots, Foliage foliage, FoliageTransactionBlock foliageTransactionBlock,
                 Proof infusedChallengeChainIpProof, RewardChainBlock rewardChainBlock, Proof rewardChainIpProof,
                 Proof rewardChainSpProof, String transactionsGenerator, List<String> transactionsGeneratorRefList,
                 TransactionInfo transactionsInfo) {
        this(headerHash, challengeChainIpProof, challengeChainSpProof, finishedSubSlots, foliage,
                foliageTransactionBlock, infusedChallengeChainIpProof, rewardChainBlock, rewardChainIpProof,
                rewardChainSpProof, transactionsGenerator,
                transactionsGeneratorRefList == null ? List.of() : transactionsGeneratorRefList.stream().map(Long::valueOf).toList(),
                transactionsInfo, null, 0);
    }
}
