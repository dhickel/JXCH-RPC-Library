package io.mindspice.jxch.rpc.schemas.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.mindspice.jxch.rpc.schemas.shared.ExactIntegerDeserializer;


public record Syncing(
        @JsonProperty("initial") boolean initial,
        @JsonProperty("plot_files_total") @JsonDeserialize(using = ExactIntegerDeserializer.class) long plotFilesTotal,
        @JsonProperty("plot_files_processed") @JsonDeserialize(using = ExactIntegerDeserializer.class) long plotFilesProcessed
) { }

