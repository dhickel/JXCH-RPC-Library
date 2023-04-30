package io.mindspice.schemas.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.Collections;
import java.util.List;


public record HarvesterDetails(
        @JsonProperty("failed_to_open_filenames") List<String> failedToOpenFilenames,
        @JsonProperty("duplicates") List<String> duplicates,
        @JsonProperty("total_plot_size") BigInteger totalPlotSize,
        @JsonProperty("syncing") Syncing syncing,
        @JsonProperty("no_key_filenames") List<String> noKeyFilenames,
        @JsonProperty("last_sync_time") double lastSyncTime,
        @JsonProperty("connection") HarvesterConnection harvesterConnection,
        @JsonProperty("plots") List<Plot> plots
) {
    public HarvesterDetails {
        failedToOpenFilenames = failedToOpenFilenames != null ? Collections.unmodifiableList(failedToOpenFilenames) : List.of();
        duplicates = duplicates != null ? Collections.unmodifiableList(duplicates) : List.of();
        noKeyFilenames = noKeyFilenames != null ? Collections.unmodifiableList(noKeyFilenames) : List.of();
        plots = plots != null ? Collections.unmodifiableList(plots) : List.of();
    }
}