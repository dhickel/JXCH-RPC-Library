package io.mindspice.jxch.rpc.schemas.harvester;

import com.fasterxml.jackson.annotation.JsonProperty;


public record HarvesterConfig(
        @JsonProperty("gpu_index") int gpuIndex,
        @JsonProperty("disable_cpu_affinity") boolean disableCpuAffinity,
        @JsonProperty("parallel_decompressor_count") int parallelDecompressorCount,
        @JsonProperty("use_gpu_harvesting") boolean useGpuHarvesting,
        @JsonProperty("enforce_gpu_index") boolean enforceGpuIndex,
        @JsonProperty("decompressor_thread_count") int decompressorThreadCount,
        @JsonProperty("recursive_plot_scan") boolean recursivePlotScan,
        @JsonProperty("refresh_parameter_interval_seconds") int refreshParameterIntervalSeconds,
        @JsonProperty("success") boolean success,
        @JsonProperty("error") String error
) {
}