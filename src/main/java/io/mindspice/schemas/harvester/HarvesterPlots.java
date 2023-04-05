package io.mindspice.schemas.harvester;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.mindspice.schemas.farmer.Plot;

import java.util.Collections;
import java.util.List;


public record HarvesterPlots(
        @JsonProperty("failed_to_open_filenames") List<String> failedToOpenFilenames,
        @JsonProperty("not_found_filenames") List<String> notFoundFilenames,
        @JsonProperty("plots") List<Plot> plots,
        @JsonProperty("success") boolean success
) {
    public HarvesterPlots {
        failedToOpenFilenames = failedToOpenFilenames !=null
                ? Collections.unmodifiableList(failedToOpenFilenames)
                : List.of();

        notFoundFilenames = notFoundFilenames !=null
                ? Collections.unmodifiableList(notFoundFilenames)
                : List.of();

        plots = plots !=null
                ? Collections.unmodifiableList(plots)
                : List.of();
    }
}
