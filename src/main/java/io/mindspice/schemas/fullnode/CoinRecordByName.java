package io.mindspice.schemas.fullnode;

import io.mindspice.enums.endpoints.Endpoint;
import io.mindspice.schemas.common.Coin;


public record CoinRecordByName(
        Coin coin_record,
        boolean success,
        String error
) {
}


