package io.mindspice.schemas.fullnode;

import com.fasterxml.jackson.annotation.JsonProperty;


public record Network(
        @JsonProperty("network_name") String networkName,
        @JsonProperty("network_prefix") String networkPrefix
) { }
