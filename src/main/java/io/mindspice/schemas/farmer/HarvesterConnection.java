package io.mindspice.schemas.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;


public record HarvesterConnection(
        @JsonProperty("port") int port,
        @JsonProperty("host") String host,
        @JsonProperty("node_id") String nodeId
) { }

