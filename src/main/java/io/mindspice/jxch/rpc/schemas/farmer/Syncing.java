package io.mindspice.jxch.rpc.schemas.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;


public record Syncing(
        @JsonProperty("initial") boolean initial,
        @JsonProperty("plot_files_total") int plotFilesTotal,
        @JsonProperty("plot_files_processed") int plotFilesProcessed
) { }

