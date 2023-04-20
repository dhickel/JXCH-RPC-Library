package io.mindspice.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.schemas.object.Coin;
import io.mindspice.schemas.object.SpendBundle;


public record SimpleTransaction(
        @JsonProperty("spend_bundle") SpendBundle spendBundle,
        @JsonProperty("additions") Coin additions,
        @JsonProperty("removals") Coin removals
) { }
