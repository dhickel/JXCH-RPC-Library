package io.mindspice.schemas.fullnode;

import io.mindspice.enums.ChiaService;
import io.mindspice.enums.ResponseType;
import io.mindspice.enums.endpoints.Endpoint;
import io.mindspice.enums.endpoints.FullNode;
import io.mindspice.schemas.ApiResponse;
import io.mindspice.schemas.components.Coin;


public record CoinSolution(
        Coin coin,
        String puzzle_reveal,
        String solution
) implements ApiResponse {
    private static final Endpoint endpoint = FullNode.GET_PUZZLE_AND_SOLUTION;


    @Override
    public ChiaService getService() {
        return ChiaService.FULL_NODE;
    }


    @Override
    public ChiaService.SubService getSubService() {
        return ChiaService.SubService.COINS;
    }


    @Override
    public ResponseType getResponseType() {
        return ResponseType.COIN_SOLUTION;
    }
}
