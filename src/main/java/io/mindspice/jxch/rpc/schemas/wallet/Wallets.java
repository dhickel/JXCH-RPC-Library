package io.mindspice.jxch.rpc.schemas.wallet;

import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Wallets(
	@JsonProperty("success") boolean success,
	@JsonProperty("fingerprint") long fingerprint,
	@JsonProperty("wallets") List<Wallet> wallets
) {
	public Wallets {
		wallets = wallets != null ? Collections.unmodifiableList(wallets) : List.of();
	}
}