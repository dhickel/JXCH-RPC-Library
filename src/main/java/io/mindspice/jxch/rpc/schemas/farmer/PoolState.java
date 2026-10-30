package io.mindspice.jxch.rpc.schemas.farmer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.mindspice.jxch.rpc.schemas.shared.ExactIntegerDeserializer;
import java.math.BigInteger;
import java.util.List;

public record PoolState(
        @JsonProperty("pool_config") PoolConfig poolConfig,
        @JsonProperty("plot_count") @JsonDeserialize(using = ExactIntegerDeserializer.class) Number plotCount,
        @JsonProperty("current_points") @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger currentPoints,
        @JsonProperty("p2_singleton_puzzle_hash") String p2SingletonPuzzleHash,
        @JsonProperty("points_acknowledged_24h") List<PointEvent> pointsAcknowledged24h,
        @JsonProperty("points_found_24h") List<PointEvent> pointsFound24h,
        @JsonProperty("authentication_token_timeout") @JsonDeserialize(using = ExactIntegerDeserializer.class) Long authenticationTokenTimeout,
        @JsonProperty("current_difficulty") @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger currentDifficulty,
        @JsonProperty("points_found_since_start") @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger pointsFoundSinceStart,
        @JsonProperty("pool_errors_24h") List<ErrorEvent> poolErrors24h,
        @JsonProperty("next_pool_info_update") double nextPoolInfoUpdate,
        @JsonProperty("points_acknowledged_since_start") @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger pointsAcknowledgedSinceStart,
        @JsonProperty("next_farmer_update") double nextFarmerUpdate,
        @JsonProperty("valid_partials_since_start") @JsonDeserialize(using = ExactIntegerDeserializer.class) Number validPartialsSinceStart,
        @JsonProperty("valid_partials_24h") List<PointEvent> validPartials24h,
        @JsonProperty("invalid_partials_since_start") @JsonDeserialize(using = ExactIntegerDeserializer.class) Number invalidPartialsSinceStart,
        @JsonProperty("invalid_partials_24h") List<PointEvent> invalidPartials24h,
        @JsonProperty("insufficient_partials_since_start") @JsonDeserialize(using = ExactIntegerDeserializer.class) Number insufficientPartialsSinceStart,
        @JsonProperty("insufficient_partials_24h") List<PointEvent> insufficientPartials24h,
        @JsonProperty("stale_partials_since_start") @JsonDeserialize(using = ExactIntegerDeserializer.class) Number stalePartialsSinceStart,
        @JsonProperty("stale_partials_24h") List<PointEvent> stalePartials24h,
        @JsonProperty("missing_partials_since_start") @JsonDeserialize(using = ExactIntegerDeserializer.class) Number missingPartialsSinceStart,
        @JsonProperty("missing_partials_24h") List<PointEvent> missingPartials24h
) {
    public PoolState {
        ExactIntegerDeserializer.requireIntegral(plotCount);
        ExactIntegerDeserializer.requireIntegral(validPartialsSinceStart);
        ExactIntegerDeserializer.requireIntegral(invalidPartialsSinceStart);
        ExactIntegerDeserializer.requireIntegral(insufficientPartialsSinceStart);
        ExactIntegerDeserializer.requireIntegral(stalePartialsSinceStart);
        ExactIntegerDeserializer.requireIntegral(missingPartialsSinceStart);
        pointsAcknowledged24h = pointsAcknowledged24h == null ? List.of() : List.copyOf(pointsAcknowledged24h);
        pointsFound24h = pointsFound24h == null ? List.of() : List.copyOf(pointsFound24h);
        poolErrors24h = poolErrors24h == null ? List.of() : List.copyOf(poolErrors24h);
        validPartials24h = validPartials24h == null ? List.of() : List.copyOf(validPartials24h);
        invalidPartials24h = invalidPartials24h == null ? List.of() : List.copyOf(invalidPartials24h);
        insufficientPartials24h = insufficientPartials24h == null ? List.of() : List.copyOf(insufficientPartials24h);
        stalePartials24h = stalePartials24h == null ? List.of() : List.copyOf(stalePartials24h);
        missingPartials24h = missingPartials24h == null ? List.of() : List.copyOf(missingPartials24h);
    }

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    @JsonPropertyOrder({"timestamp", "points"})
    public record PointEvent(
            @JsonDeserialize(using = ExactIntegerDeserializer.class) long timestamp,
            @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger points) { }

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    @JsonPropertyOrder({"timestamp", "error"})
    public record ErrorEvent(
            @JsonDeserialize(using = ExactIntegerDeserializer.class) long timestamp,
            JsonNode error) { }

    public record PoolError(
            @JsonProperty("error_message") String errorMessage,
            @JsonProperty("error_code") @JsonDeserialize(using = ExactIntegerDeserializer.class) int errorCode
    ) { }

    public record PoolConfig(
            @JsonProperty("target_puzzle_hash") String targetPuzzleHash,
            @JsonProperty("authentication_public_key") String authenticationPublicKey,
            @JsonProperty("launcher_id") String launcherId,
            @JsonProperty("p2_singleton_puzzle_hash") String p2SingletonPuzzleHash,
            @JsonProperty("payout_instructions") String payoutInstructions,
            @JsonProperty("owner_public_key") String ownerPublicKey,
            @JsonProperty("pool_url") String poolUrl,
            @JsonProperty("key_derivation_index") @JsonDeserialize(using = ExactIntegerDeserializer.class) Number keyDerivationIndex,
            @JsonProperty("version") @JsonDeserialize(using = ExactIntegerDeserializer.class) Number version
    ) {
        public PoolConfig {
            ExactIntegerDeserializer.requireIntegral(keyDerivationIndex);
            ExactIntegerDeserializer.requireIntegral(version);
        }
    }
}
