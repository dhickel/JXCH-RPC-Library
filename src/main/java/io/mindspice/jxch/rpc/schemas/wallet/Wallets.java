package io.mindspice.jxch.rpc.schemas.wallet;

import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Wallets(
	@JsonProperty("success") boolean success,
	@JsonProperty("fingerprint") Long fingerprint,
	@JsonProperty("wallets") List<Wallet> wallets
) {
	public Wallets(boolean success, long fingerprint, List<Wallet> wallets) {
		this(success, Long.valueOf(fingerprint), wallets);
	}

	public Wallets {
		wallets = wallets != null ? Collections.unmodifiableList(wallets) : List.of();
	}
}