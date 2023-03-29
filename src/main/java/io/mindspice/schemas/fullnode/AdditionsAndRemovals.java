package io.mindspice.schemas.fullnode;

import io.mindspice.enums.ChiaService;
import io.mindspice.enums.endpoints.Endpoint;
import io.mindspice.enums.endpoints.FullNode;
import io.mindspice.schemas.ApiResponse;
import io.mindspice.schemas.common.Coin;


public record AdditionsAndRemovals (
        Additions[] additions,
        Removals[] removals,
        boolean success,
        String error
) implements ApiResponse {
    private static final Endpoint endpoint = FullNode.GET_ADDITIONS_AND_REMOVALS;


    @Override
    public Endpoint getEndpoint() {
        return endpoint;
    }


    @Override
    public ChiaService getService() {
        return endpoint.getService();
    }


    public record Additions(
            Coin coin,
            int confirmed_block_index,
            int spent_block_index,
            boolean spent,
            boolean coinbase,
            int timestamp) { }


    public record Removals(
            Coin coin,
            int confirmed_block_index,
            int spent_block_index,
            boolean spent,
            boolean coinbase,
            int timestamp) { }
}
