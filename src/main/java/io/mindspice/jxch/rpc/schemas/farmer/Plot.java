package io.mindspice.jxch.rpc.schemas.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.mindspice.jxch.rpc.schemas.shared.ExactIntegerDeserializer;
import java.math.BigInteger;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Plot (
        @JsonProperty("pool_contract_puzzle_hash") String poolContractPuzzleHash,
        @JsonProperty("time_modified") @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger timeModified,
        @JsonProperty("plot_public_key") String plotPublicKey,
        @JsonProperty("filename") String filename,
        @JsonProperty("size") @JsonDeserialize(using = ExactIntegerDeserializer.class) int size,
        @JsonProperty("pool_public_key") String poolPublicKey,
        @JsonProperty("file_size") @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger fileSize,
        @JsonProperty("plot_id") String plotId,
        @JsonProperty("compression_level") @JsonDeserialize(using = ExactIntegerDeserializer.class) Integer compressionLevel,
        @JsonProperty("plot_index") @JsonDeserialize(using = ExactIntegerDeserializer.class) Integer plotIndex,
        @JsonProperty("meta_group") @JsonDeserialize(using = ExactIntegerDeserializer.class) Integer metaGroup
) {
    public Plot(String poolContractPuzzleHash, long timeModified, String plotPublicKey, String filename,
            int size, String poolPublicKey, Long fileSize, String plotId, Integer compressionLevel,
            Integer plotIndex, Integer metaGroup) {
        this(poolContractPuzzleHash, BigInteger.valueOf(timeModified), plotPublicKey, filename, size,
                poolPublicKey, fileSize == null ? null : BigInteger.valueOf(fileSize), plotId,
                compressionLevel, plotIndex, metaGroup);
    }

    public Plot(String poolContractPuzzleHash, int timeModified, String plotPublicKey, String filename,
            int size, String poolPublicKey, Long fileSize, String plotId) {
        this(poolContractPuzzleHash, (long) timeModified, plotPublicKey, filename, size, poolPublicKey, fileSize,
                plotId, null, null, null);
    }
}

