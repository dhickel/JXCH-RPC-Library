package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.jxch.rpc.schemas.object.Coin;

import java.util.Collections;
import java.util.List;
import java.math.BigInteger;


public record Wallet(
	@JsonProperty("data") String data,
	@JsonProperty("name") String name,
	@JsonProperty("id") long id,
	@JsonProperty("type") int type,
	@JsonProperty("authorized_providers") List<String> authorizedProviders,
	@JsonProperty("flags_needed") List<String> flagsNeeded
) {
	public Wallet {
		authorizedProviders = authorizedProviders != null ? List.copyOf(authorizedProviders) : List.of();
		flagsNeeded = flagsNeeded != null ? List.copyOf(flagsNeeded) : List.of();
	}

	public Wallet(String data, String name, int id, int type) {
		this(data, name, id, type, List.of(), List.of());
	}

	public record Data(
			@JsonProperty("did_id") String didId,
			@JsonProperty("temp_pubkey") String tempPubkey,
			@JsonProperty("metadata") String metadata,
			@JsonProperty("backup_ids") List<String> backupIds,
			@JsonProperty("current_inner") String currentInner,
			@JsonProperty("temp_coin") Coin tempCoin,
			@JsonProperty("sent_recovery_transaction") boolean sentRecoveryTransaction,
			@JsonProperty("num_of_backup_ids_needed") BigInteger numOfBackupIdsNeeded,
			@JsonProperty("parent_info") List<List<Object>> parentInfo,
			@JsonProperty("origin_coin") Coin originCoin,
			@JsonProperty("temp_puzhash") String tempPuzhash
	) {
		public Data {
			backupIds = backupIds != null ? Collections.unmodifiableList(backupIds) : List.of();
			parentInfo = parentInfo != null ? Collections.unmodifiableList(parentInfo) : List.of();
		}
	}
}