package io.mindspice.jxch.rpc.schemas.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record LogLevel(String level,
        @JsonProperty("available_levels") List<String> availableLevels,
        List<String> errors) {
    public LogLevel {
        availableLevels = availableLevels == null ? List.of() : List.copyOf(availableLevels);
        errors = errors == null ? List.of() : List.copyOf(errors);
    }
}
