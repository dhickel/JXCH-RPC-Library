package io.mindspice.schemas.fullnode;

import io.mindspice.enums.ChiaService;
import io.mindspice.enums.endpoints.Endpoint;
import io.mindspice.enums.endpoints.FullNode;
import io.mindspice.schemas.ApiResponse;
import io.mindspice.schemas.Objects.Coin;
import io.mindspice.schemas.Objects.Output;
import io.mindspice.schemas.Objects.VDF;

import java.math.BigInteger;
import java.util.List;


//FIXME Epoch Summary?
public record BlockChainState(
        String node_id,
        int difficulty,
        boolean genesis_challenge_initialized,
        long mempool_size,
        long mempool_cost,
        long mempool_fees,
        MemPoolFees mempool_min_fees,
        long mempool_max_total_cost,
        long block_max_cost,
        Peak peak,
        BigInteger space,
        long sub_slot_iters,
        Sync sync
) implements ApiResponse {
    @Override
    public ChiaService getService() {
        return ChiaService.FULL_NODE;
    }


    @Override
    public ChiaService.SubService getSubService() {
        return ChiaService.SubService.METRICS;
    }


    @Override
    public Endpoint getEndPoint() {
        return FullNode.GET_BLOCKCHAIN_STATE;
    }


    record Peak(
            String challenge_block_info_hash,
            Output challenge_vdf_output,
            int deficit,
            String farmer_puzzle_hash,
            long fees,
            String[] finished_challenge_slot_hashes,
            String[] finished_infused_challenge_slot_hashes,
            String[] finished_reward_slot_hashes,
            String header_hash,
            int height,
            Output infused_challenge_vdf_output,
            boolean overflow,
            String pool_puzzle_hash,
            String prev_hash,
            String prev_transaction_block_hash,
            int prev_transaction_block_height,
            long required_iters,
            List<Coin> reward_claims_incorporated,
            String reward_infusion_new_challenge,
            int signage_point_index,
            boolean sub_epoch_summary_included,
            long sub_slot_iters,
            int timestamp,
            long total_iters,
            long weight
    ) { }


    record Sync(
            boolean sync_mode,
            int sync_progress_height,
            int sync_tip_height,
            boolean synced
    ) { }


    record MemPoolFees(long cost_5000000) { }
}