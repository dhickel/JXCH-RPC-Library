package io.mindspice.jxch.rpc.schemas.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Plot (
        @JsonProperty("pool_contract_puzzle_hash") String poolContractPuzzleHash,
        @JsonProperty("time_modified") int timeModified,
        @JsonProperty("plot_public_key") String plotPublicKey,
        @JsonProperty("filename") String filename,
        @JsonProperty("size") int size,
        @JsonProperty("pool_public_key") String poolPublicKey,
        @JsonProperty("file_size") Long fileSize,
        @JsonProperty("plot_id") String plotId
) { }

