package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.jxch.rpc.schemas.object.Coin;
import io.mindspice.jxch.rpc.schemas.object.SpendBundle;
import java.util.List;


@Deprecated(since = "2.7.5")
public record SimpleTransaction(
        @JsonProperty("spend_bundle") SpendBundle spendBundle,
        @JsonProperty("additions") List<Coin> additions,
        @JsonProperty("removals") List<Coin> removals
) {
    public SimpleTransaction(SpendBundle spendBundle, Coin additions, Coin removals) {
        this(spendBundle, additions == null ? List.of() : List.of(additions),
                removals == null ? List.of() : List.of(removals));
    }
}
