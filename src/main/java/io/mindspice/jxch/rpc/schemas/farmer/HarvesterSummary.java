package io.mindspice.jxch.rpc.schemas.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigInteger;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record HarvesterSummary(
        @JsonProperty("connection") HarvesterConnection harvesterConnection,
        @JsonProperty("duplicates") int duplicates,
        @JsonProperty("failed_to_open_filenames") int failedToOpenFilenames,
        @JsonProperty("last_sync_time") double lastSyncTime,
        @JsonProperty("no_key_filenames") int noKeyFilenames,
        @JsonProperty("plots") int plots,
        @JsonProperty("syncing") Syncing syncing,
        @JsonProperty("total_plot_size") BigInteger totalPlotSize
) { }
