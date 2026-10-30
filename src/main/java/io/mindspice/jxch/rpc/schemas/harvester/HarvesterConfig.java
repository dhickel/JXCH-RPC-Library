package io.mindspice.jxch.rpc.schemas.harvester;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.mindspice.jxch.rpc.schemas.shared.ExactIntegerDeserializer;


public record HarvesterConfig(
        @JsonProperty("gpu_index") @JsonDeserialize(using = ExactIntegerDeserializer.class) Number gpuIndex,
        @JsonProperty("disable_cpu_affinity") boolean disableCpuAffinity,
        @JsonProperty("parallel_decompressor_count") @JsonDeserialize(using = ExactIntegerDeserializer.class) Number parallelDecompressorCount,
        @JsonProperty("use_gpu_harvesting") boolean useGpuHarvesting,
        @JsonProperty("enforce_gpu_index") boolean enforceGpuIndex,
        @JsonProperty("decompressor_thread_count") @JsonDeserialize(using = ExactIntegerDeserializer.class) Number decompressorThreadCount,
        @JsonProperty("recursive_plot_scan") boolean recursivePlotScan,
        @JsonProperty("refresh_parameter_interval_seconds") @JsonDeserialize(using = ExactIntegerDeserializer.class) Long refreshParameterIntervalSeconds,
        @JsonProperty("success") boolean success,
        @JsonProperty("error") String error
) {
    public HarvesterConfig {
        ExactIntegerDeserializer.requireIntegral(gpuIndex);
        ExactIntegerDeserializer.requireIntegral(parallelDecompressorCount);
        ExactIntegerDeserializer.requireIntegral(decompressorThreadCount);
    }
}