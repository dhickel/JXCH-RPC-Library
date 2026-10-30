package io.mindspice.jxch.rpc.schemas.wallet.did;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.List;


public record DIDInfo(
        @JsonProperty("public_key") String publicKey,
        @JsonProperty("p2_address") String p2Address,
        @JsonProperty("metadata") JsonNode metadata,
        @JsonProperty("solution") JsonNode solution,
        @JsonProperty("hints") List<String> hints,
        @JsonProperty("launcher_id") String launcherId,
        @JsonProperty("success") boolean success,
        @JsonProperty("full_puzzle") String fullPuzzle,
        @JsonProperty("recovery_list_hash") String recoveryListHash,
        @JsonProperty("latest_coin") String latestCoin,
        @JsonProperty("num_verification") int numVerification,
        @JsonProperty("did_id") String didId
) {

    public DIDInfo(String publicKey, String p2Address, JsonNode metadata, JsonNode solution,
            List<String> hints, String launcherId, boolean success, String fullPuzzle,
            String recoveryListHash, String latestCoin, int numVerification) {
        this(publicKey, p2Address, metadata, solution, hints, launcherId, success, fullPuzzle,
                recoveryListHash, latestCoin, numVerification, null);
    }

    public DIDInfo {
        hints = hints != null ? Collections.unmodifiableList(hints) : List.of();
    }
}