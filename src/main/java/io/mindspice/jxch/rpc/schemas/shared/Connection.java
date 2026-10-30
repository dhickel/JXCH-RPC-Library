package io.mindspice.jxch.rpc.schemas.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public record Connection(
	@JsonProperty("creation_time") double creationTime,
	@JsonProperty("local_port") @JsonDeserialize(using = ExactIntegerDeserializer.class) Number localPort,
	@JsonProperty("bytes_read") @JsonDeserialize(using = ExactIntegerDeserializer.class) Number bytesRead,
	@JsonProperty("peer_port") @JsonDeserialize(using = ExactIntegerDeserializer.class) int peerPort,
	@JsonProperty("peak_hash") String peakHash,
	@JsonProperty("peak_weight") @JsonDeserialize(using = ExactIntegerDeserializer.class) java.math.BigInteger peakWeight,
	@JsonProperty("type") @JsonDeserialize(using = ExactIntegerDeserializer.class) Integer type,
	@JsonProperty("peer_server_port") @JsonDeserialize(using = ExactIntegerDeserializer.class) Integer peerServerPort,
	@JsonProperty("bytes_written") @JsonDeserialize(using = ExactIntegerDeserializer.class) Number bytesWritten,
	@JsonProperty("peer_host") String peerHost,
	@JsonProperty("last_message_time") double lastMessageTime,
	@JsonProperty("peak_height") @JsonDeserialize(using = ExactIntegerDeserializer.class) Long peakHeight,
	@JsonProperty("node_id") String nodeId
) {
    public Connection {
        ExactIntegerDeserializer.requireIntegral(localPort);
        ExactIntegerDeserializer.requireIntegral(bytesRead);
        ExactIntegerDeserializer.requireIntegral(bytesWritten);
    }
}