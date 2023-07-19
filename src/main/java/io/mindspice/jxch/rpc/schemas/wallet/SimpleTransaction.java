package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.jxch.rpc.schemas.object.Coin;
import io.mindspice.jxch.rpc.schemas.object.SpendBundle;


public record SimpleTransaction(
        @JsonProperty("spend_bundle") SpendBundle spendBundle,
        @JsonProperty("additions") Coin additions,
        @JsonProperty("removals") Coin removals
) { }
