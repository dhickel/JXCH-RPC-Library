package io.mindspice.schemas.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;


public record Connection(
        @JsonProperty("port") int port,
        @JsonProperty("host") String host,
        @JsonProperty("node_id") String nodeId
) { }

