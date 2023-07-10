package io.mindspice.schemas.wallet;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.schemas.object.Coin;


public record PoolWalletStatus(
        @JsonProperty("unconfirmed_transactions") List<Transaction> unconfirmedTransactions,
        @JsonProperty("success") boolean success,
        @JsonProperty("state") State state,
        @JsonProperty("error") String error
) {

    public PoolWalletStatus {
        unconfirmedTransactions = unconfirmedTransactions() != null
                ? Collections.unmodifiableList(unconfirmedTransactions)
                : List.of();
    }

    public record State(
            @JsonProperty("current") PoolInfo poolInfo,
            @JsonProperty("launcher_coin") Coin launcherCoin,
            @JsonProperty("current_inner") String currentInner,
            @JsonProperty("launcher_id") String launcherId,
            @JsonProperty("p2_singleton_puzzle_hash") String p2SingletonPuzzleHash,
            @JsonProperty("singleton_block_height") int singletonBlockHeight,
            @JsonProperty("tip_singleton_coin_id") String tipSingletonCoinId,
            @JsonProperty("target") PoolInfo target
    ) { }


    public record PoolInfo(
            @JsonProperty("target_puzzle_hash") String targetPuzzleHash,
            @JsonProperty("owner_pubkey") String ownerPubkey,
            @JsonProperty("state") int state,
            @JsonProperty("relative_lock_height") int relativeLockHeight,
            @JsonProperty("pool_url") String poolUrl,
            @JsonProperty("version") int version
    ) { }
}