package io.mindspice.schemas.fullnode;

import io.mindspice.enums.ChiaService;
import io.mindspice.enums.endpoints.Daemon;
import io.mindspice.enums.endpoints.Endpoint;
import io.mindspice.enums.endpoints.FullNode;
import io.mindspice.schemas.ApiResponse;
import io.mindspice.schemas.Objects.CoinRecord;

import java.util.Collections;
import java.util.List;


public record AdditionsAndRemovals(
        List<CoinRecord> additions,
        List<CoinRecord> removals,
        boolean success,
        String error
) implements ApiResponse {

    public AdditionsAndRemovals {
        additions = additions == null ? List.of() : Collections.unmodifiableList(additions);
        removals = removals == null ? List.of() : Collections.unmodifiableList(removals);
    }


    @Override
    public ChiaService getService() {
        return ChiaService.FULL_NODE;
    }


    @Override
    public ChiaService.SubService getSubService() {
        return ChiaService.SubService.COINS;
    }


    @Override
    public Endpoint getEndPoint() {
        return FullNode.GET_ADDITIONS_AND_REMOVALS;
    }
}