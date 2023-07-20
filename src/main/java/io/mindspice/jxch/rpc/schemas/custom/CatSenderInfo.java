package io.mindspice.jxch.rpc.schemas.custom;

import com.fasterxml.jackson.annotation.JsonProperty;


public record CatSenderInfo(
        @JsonProperty("sender_puzzle_hash") String senderPuzzleHash,
        @JsonProperty("asset_id") String assetId
) {
}
