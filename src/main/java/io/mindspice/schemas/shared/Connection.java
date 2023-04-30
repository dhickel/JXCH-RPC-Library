package io.mindspice.schemas.shared;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Connection(
	@JsonProperty("creation_time") int creationTime,
	@JsonProperty("local_port") int localPort,
	@JsonProperty("bytes_read") int bytesRead,
	@JsonProperty("peer_port") int peerPort,
	@JsonProperty("peak_hash") String peakHash,
	@JsonProperty("peak_weight") int peakWeight,
	@JsonProperty("type") int type,
	@JsonProperty("peer_server_port") int peerServerPort,
	@JsonProperty("bytes_written") int bytesWritten,
	@JsonProperty("peer_host") String peerHost,
	@JsonProperty("last_message_time") int lastMessageTime,
	@JsonProperty("peak_height") int peakHeight,
	@JsonProperty("node_id") String nodeId
) {
}