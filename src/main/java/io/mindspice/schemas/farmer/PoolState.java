package io.mindspice.schemas.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;


public record PoolState(
        @JsonProperty("pool_config") PoolConfig poolConfig,
        @JsonProperty("plot_count") int plotCount,
        @JsonProperty("current_points") int currentPoints,
        @JsonProperty("p2_singleton_puzzle_hash") String p2SingletonPuzzleHash,
        @JsonProperty("points_acknowledged_24h") List<String> pointsAcknowledged24h,
        //Fix me, need to join pool to see what this return, I think ints? leaving a string for safety
        @JsonProperty("points_found_24h") List<String> pointsFound24h,
        @JsonProperty("authentication_token_timeout") int authenticationTokenTimeout,
        @JsonProperty("current_difficulty") int currentDifficulty,
        @JsonProperty("points_found_since_start") int pointsFoundSinceStart,
        @JsonProperty("pool_errors_24h") List<PoolError> poolErrors24h,
        @JsonProperty("next_pool_info_update") int nextPoolInfoUpdate,
        @JsonProperty("points_acknowledged_since_start") int pointsAcknowledgedSinceStart,
        @JsonProperty("next_farmer_update") int nextFarmerUpdate
) {

    public PoolState {
        pointsAcknowledged24h = pointsAcknowledged24h != null
                ? Collections.unmodifiableList(pointsAcknowledged24h)
                : List.of();

        pointsFound24h = pointsFound24h != null
                ? Collections.unmodifiableList(pointsFound24h)
                : List.of();

        poolErrors24h = poolErrors24h != null
                ? Collections.unmodifiableList(poolErrors24h)
                : List.of();
    }


    public record PoolError(
            @JsonProperty("error_message") String errorMessage,
            @JsonProperty("error_code") int errorCode
    ) { }


    public record PoolConfig(
            @JsonProperty("target_puzzle_hash") String targetPuzzleHash,
            @JsonProperty("authentication_public_key") String authenticationPublicKey,
            @JsonProperty("launcher_id") String launcherId,
            @JsonProperty("p2_singleton_puzzle_hash") String p2SingletonPuzzleHash,
            @JsonProperty("payout_instructions") String payoutInstructions,
            @JsonProperty("owner_public_key") String ownerPublicKey,
            @JsonProperty("pool_url") String poolUrl
    ) {
    }

}