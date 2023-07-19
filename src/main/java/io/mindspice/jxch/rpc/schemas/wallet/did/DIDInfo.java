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
        @JsonProperty("num_verification") int numVerification
) {

    public DIDInfo {
        hints = hints != null ? Collections.unmodifiableList(hints) : List.of();
    }
}