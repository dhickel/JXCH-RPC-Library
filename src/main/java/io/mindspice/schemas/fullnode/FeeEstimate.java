package io.mindspice.schemas.fullnode;

import io.mindspice.enums.ChiaService;
import io.mindspice.enums.endpoints.Endpoint;
import io.mindspice.enums.endpoints.FullNode;
import io.mindspice.schemas.ApiResponse;

import java.util.Collections;
import java.util.List;


public record FeeEstimate(
        long mempool_fees,
        List<Integer> target_times,
        boolean full_node_synced,
        int last_tx_block_height,
        long mempool_size,
        long fee_rate_last_block,
        String error,
        int num_spends,
        long last_block_cost,
        long current_fee_rate,
        long fees_last_block,
        int node_time_utc,
        boolean success,
        int last_peak_timestamp,
        long mempool_max_size,
        int peak_height,
        List<Integer> estimates
) implements ApiResponse {

    public FeeEstimate {
        target_times = target_times == null ? List.of() : Collections.unmodifiableList(target_times);
        estimates =  estimates == null ? List.of() : Collections.unmodifiableList(estimates);
    }


    @Override
    public ChiaService getService() {
        return ChiaService.FULL_NODE;
    }


    @Override
    public ChiaService.SubService getSubService() {
        return ChiaService.SubService.FEES;
    }


    @Override
    public Endpoint getEndPoint() {
        return FullNode.GET_FEE_ESTIMATE;
    }
}