package io.mindspice.schemas.custom;

import com.fasterxml.jackson.annotation.JsonProperty;


public record NftRecipient(
        @JsonProperty("recipient_puzzle_hash") String recipientPuzzleHash
) {
}
