package io.mindspice.schemas.wallet.did;

import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public record DIDRecoveryList(
	@JsonProperty("wallet_id") int walletId,
	@JsonProperty("success") boolean success,
	@JsonProperty("recovery_list") List<String> recoverList,
	@JsonProperty("error") String error,
	@JsonProperty("num_required") int numRequired
) {

	public DIDRecoveryList {
		recoverList = recoverList != null ? Collections.unmodifiableList(recoverList) : List.of();
	}
}