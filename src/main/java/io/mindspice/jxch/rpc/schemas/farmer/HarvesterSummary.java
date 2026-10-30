package io.mindspice.jxch.rpc.schemas.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.mindspice.jxch.rpc.schemas.shared.ExactIntegerDeserializer;

import java.math.BigInteger;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record HarvesterSummary(
        @JsonProperty("connection") HarvesterConnection harvesterConnection,
        @JsonProperty("duplicates") @JsonDeserialize(using = ExactIntegerDeserializer.class) Number duplicates,
        @JsonProperty("failed_to_open_filenames") @JsonDeserialize(using = ExactIntegerDeserializer.class) Number failedToOpenFilenames,
        @JsonProperty("last_sync_time") Double lastSyncTime,
        @JsonProperty("no_key_filenames") @JsonDeserialize(using = ExactIntegerDeserializer.class) Number noKeyFilenames,
        @JsonProperty("plots") @JsonDeserialize(using = ExactIntegerDeserializer.class) Number plots,
        @JsonProperty("syncing") Syncing syncing,
        @JsonProperty("total_plot_size") @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger totalPlotSize,
        @JsonProperty("total_effective_plot_size") @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger totalEffectivePlotSize,
        @JsonProperty("harvesting_mode") @JsonDeserialize(using = ExactIntegerDeserializer.class) Integer harvestingMode
) {
    public HarvesterSummary {
        ExactIntegerDeserializer.requireIntegral(duplicates);
        ExactIntegerDeserializer.requireIntegral(failedToOpenFilenames);
        ExactIntegerDeserializer.requireIntegral(noKeyFilenames);
        ExactIntegerDeserializer.requireIntegral(plots);
    }
}
