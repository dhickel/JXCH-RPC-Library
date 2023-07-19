package io.mindspice.jxch.rpc.schemas.wallet.cat;

import com.fasterxml.jackson.annotation.JsonProperty;


public record StrayCat(
        @JsonProperty("sender_puzzle_hash") String senderPuzzleHash,
        @JsonProperty("name") String name,
        @JsonProperty("first_seen_height") int firstSeenHeight,
        @JsonProperty("asset_id") String assetId
) {
}