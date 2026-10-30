package io.mindspice.jxch.rpc.schemas.crawler;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.mindspice.jxch.rpc.schemas.shared.ExactIntegerDeserializer;
import java.math.BigInteger;
import java.util.List;

public record IpsAfterTimestamp(List<String> ips,
                                @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger total) {
    public IpsAfterTimestamp { ips = ips == null ? List.of() : List.copyOf(ips); }
}
