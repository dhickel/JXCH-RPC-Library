package io.mindspice.jxch.rpc.schemas.crawler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.mindspice.jxch.rpc.schemas.shared.ExactIntegerDeserializer;
import java.math.BigInteger;
import java.util.Map;

public record PeerCounts(
        @JsonProperty("total_last_5_days") @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger totalLast5Days,
        @JsonProperty("reliable_nodes") @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger reliableNodes,
        @JsonProperty("ipv4_last_5_days") @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger ipv4Last5Days,
        @JsonProperty("ipv6_last_5_days") @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger ipv6Last5Days,
        @JsonDeserialize(contentUsing = ExactIntegerDeserializer.class) Map<String, BigInteger> versions) {
    public PeerCounts { versions = versions == null ? Map.of() : Map.copyOf(versions); }
}
