package io.mindspice.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.schemas.object.Coin;

import java.util.Collections;
import java.util.List;


public record Wallet(
	@JsonProperty("data") String data,
	@JsonProperty("name") String name,
	@JsonProperty("id") int id,
	@JsonProperty("type") int type
) {

	public record Data(
			@JsonProperty("did_id") String didId,
			@JsonProperty("temp_pubkey") String tempPubkey,
			@JsonProperty("metadata") String metadata,
			@JsonProperty("backup_ids") List<Object> backupIds,
			@JsonProperty("current_inner") String currentInner,
			@JsonProperty("temp_coin") Coin tempCoin,
			@JsonProperty("sent_recovery_transaction") boolean sentRecoveryTransaction,
			@JsonProperty("num_of_backup_ids_needed") int numOfBackupIdsNeeded,
			@JsonProperty("parent_info") List<List<String>> parentInfo,
			@JsonProperty("origin_coin") Coin originCoin,
			@JsonProperty("temp_puzhash") String tempPuzhash
	) {
		public Data {
			backupIds = backupIds() != null ? Collections.unmodifiableList(backupIds) : List.of();
			parentInfo = parentInfo() != null ? Collections.unmodifiableList(parentInfo) : List.of();
		}
	}
}