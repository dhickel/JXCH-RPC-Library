package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PuzzleAnnouncement(
	@JsonProperty("morph_bytes") String morphBytes,
	@JsonProperty("message") String message,
	@JsonProperty("puzzle_hash") String puzzleHash
) {
}