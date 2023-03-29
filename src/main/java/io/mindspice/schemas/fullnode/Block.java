package io.mindspice.schemas.fullnode;

import io.mindspice.enums.ChiaService;
import io.mindspice.enums.endpoints.Endpoint;
import io.mindspice.enums.endpoints.FullNode;
import io.mindspice.schemas.ApiResponse;
import io.mindspice.schemas.common.Coin;
import io.mindspice.schemas.common.Output;
import io.mindspice.schemas.common.VDF;

import java.math.BigInteger;
import java.util.List;


public record Block(
        Proof challenge_chain_ip_proof,
        Proof challenge_chain_sp_proof,
        List<FinishedSubSlot> finished_sub_slots,
        Foliage foliage,
        FoliageTransactionBlock foliage_transaction_block,
        RewardChainBlock reward_chain_block,
        TransactionInfo transaction_info,
        Proof infused_challenge_chain_ip_proof,
        Proof reward_chain_ip_proof,
        Proof reward_chain_sp_proof,
        String transactions_generator,
        List<String> transactions_generator_ref_list,
        TransactionInfo transactions_info
) {


    record ChallengeChainIpProof(
            boolean normalized_to_identity,
            String witness,
            int witness_type
    ) implements ApiResponse {

        private static final Endpoint endpoint = FullNode.GET_BLOCK;


        @Override
        public Endpoint getEndpoint() {
            return endpoint;
        }


        @Override
        public ChiaService getService() {
            return endpoint.getService();
        }
    }


    record Proof(
            boolean normalized_to_identity,
            String witness,
            int witness_type
    ) { }


    record ChallengeChainSpProof(
            boolean normalized_to_identity,
            String witness,
            int witness_type
    ) { }


    record FinishedSubSlot(
            ChallengeChain challenge_chain,
            InfusedChallengeChain infused_challenge_chain,
            Proofs proofs,
            RewardChain reward_chain
    ) { }


    record ChallengeChain(
            EndOfSlotVdf challenge_chain_end_of_slot_vdf,
            String infused_challenge_chain_sub_slot_hash,
            Integer new_difficulty,
            Integer new_sub_slot_iters,
            String subepoch_summary_hash
    ) { }


    record InfusedChallengeChain(
            EndOfSlotVdf infused_challenge_chain_end_of_slot_vdf
    ) { }


    record EndOfSlotVdf(
            String challenge,
            long number_of_iterations,
            Output output
    ) { }


    record Proofs(
            Proof challenge_chain_slot_proof,
            Proof infused_challenge_chain_slot_proof,
            Proof reward_chain_slot_proof
    ) { }


    record RewardChain(
            String challenge_chain_sub_slot_hash,
            int deficit,
            EndOfSlotVdf end_of_slot_vdf,
            String infused_challenge_chain_sub_slot_hash
    ) { }


    record Foliage(
            FoliageBlockData foliage_block_data,
            String foliage_block_data_signature,
            String foliage_transaction_block_signature,
            String foliage_transaction_block_hash,
            String prev_block_hash,
            String reward_block_hash
    ) { }


    record FoliageBlockData(
            String extension_data,
            String farmer_reward_puzzle_hash,
            String pool_signature,
            PoolTarget pool_target,
            String unfinished_reward_block_hash
    ) { }


    record PoolTarget(
            int max_height,
            String puzzle_hash
    ) { }


    record FoliageTransactionBlock(
            String additions_root,
            String foliage_transaction_block_signature,
            String transaction_data_hash,
            String filter_hash,
            String prev_transaction_block_hash,
            String removals_root,
            long timestamp,
            String transactions_info_hash
    ) { }


    public record RewardChainBlock(
            VDF challenge_chain_ip_vdf,
            String challenge_chain_sp_signature,
            VDF challenge_chain_sp_vdf,
            long height,
            Object infused_challenge_chain_ip_vdf,
            boolean is_transaction_block,
            String pos_ss_cc_challenge_hash,
            ProofOfSpace proof_of_space,
            VDF reward_chain_ip_vdf,
            String reward_chain_sp_signature,
            VDF reward_chain_sp_vdf,
            int signage_point_index,
            BigInteger total_iters,
            long weight
    ) { }


    public record ProofOfSpace(
            String challenge,
            String plot_public_key,
            Object pool_contract_puzzle_hash,
            String pool_public_key,
            String proof,
            int size
    ) { }


    record TransactionInfo(
            String generator_root,
            String generator_refs_root,
            String aggregated_signature,
            String fees,
            String cost,
            List<Coin> reward_claims_incorporated
    ) { }
}





