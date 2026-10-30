package io.mindspice.jxch.rpc.schemas.wallet.did;

import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

@Deprecated(since = "2.7.5")
public record DIDRecoveryInfo(
	@JsonProperty("newpuzhash") String newpuzhash,
	@JsonProperty("wallet_id") int walletId,
	@JsonProperty("coin_name") String coinName,
	@JsonProperty("success") boolean success,
	@JsonProperty("backup_dids") List<String> backupDids,
	@JsonProperty("my_did") String myDid,
	@JsonProperty("error") String error,
	@JsonProperty("pubkey") String pubkey
) {

	public DIDRecoveryInfo {
		backupDids = backupDids != null ? Collections.unmodifiableList(backupDids) : List.of();
	}
}