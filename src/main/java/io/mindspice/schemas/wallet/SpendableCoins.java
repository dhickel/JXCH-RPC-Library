package io.mindspice.schemas.wallet;

import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.schemas.object.Coin;
import io.mindspice.schemas.object.CoinRecord;



public record SpendableCoins(
	@JsonProperty("unconfirmed_additions") List<Coin> unconfirmedAdditions,
	@JsonProperty("success") boolean success,
	@JsonProperty("confirmed_records") List<CoinRecord> confirmedRecords,
	@JsonProperty("unconfirmed_removals") List<CoinRecord> unconfirmedRemovals,
	@JsonProperty("error") String error
) {
	public SpendableCoins {
		unconfirmedAdditions = unconfirmedAdditions != null
				? Collections.unmodifiableList(unconfirmedAdditions)
				: List.of();

		confirmedRecords = confirmedRecords != null
				? Collections.unmodifiableList(confirmedRecords)
				: List.of();

		unconfirmedRemovals = unconfirmedRemovals != null
				? Collections.unmodifiableList(unconfirmedRemovals)
				: List.of();
	}
}