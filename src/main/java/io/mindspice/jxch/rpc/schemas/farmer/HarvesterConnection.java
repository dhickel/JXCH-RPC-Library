package io.mindspice.jxch.rpc.schemas.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.mindspice.jxch.rpc.schemas.shared.ExactIntegerDeserializer;


public record HarvesterConnection(
        @JsonProperty("port") @JsonDeserialize(using = ExactIntegerDeserializer.class) int port,
        @JsonProperty("host") String host,
        @JsonProperty("node_id") String nodeId
) { }

