package io.mindspice.jxch.rpc.schemas.wallet;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PuzzleAnnouncement(
	@Deprecated @JsonProperty("morph_bytes") String morphBytes,
	@JsonProperty("message") String message,
	@JsonProperty("puzzle_hash") String puzzleHash
) {
    public PuzzleAnnouncement(String message, String puzzleHash) {
        this(null, message, puzzleHash);
    }
}