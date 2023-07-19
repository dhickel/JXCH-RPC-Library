package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SyncStatus(
	@JsonProperty("synced") boolean synced,
	@JsonProperty("syncing") boolean syncing,
	@JsonProperty("success") boolean success,
	@JsonProperty("error") String error,
	@JsonProperty("genesis_initialized") boolean genesisInitialized
) { }