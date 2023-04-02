package io.mindspice.schemas.object;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
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
        @JsonProperty("transactions_generator_ref_list") List<String> transactionsGeneratorRefList,
        @JsonProperty("transactions_info") TransactionInfo transactionsInfo
) {
    public Block {
        finishedSubSlots = finishedSubSlots != null
                ? Collections.unmodifiableList(finishedSubSlots)
                : List.of();

        transactionsGeneratorRefList = transactionsGeneratorRefList != null
                ? Collections.unmodifiableList(transactionsGeneratorRefList)
                : List.of();
    }
}