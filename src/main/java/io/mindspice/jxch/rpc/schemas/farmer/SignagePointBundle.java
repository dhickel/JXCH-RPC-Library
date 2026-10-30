package io.mindspice.jxch.rpc.schemas.farmer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.mindspice.jxch.rpc.schemas.shared.ExactIntegerDeserializer;
import io.mindspice.jxch.rpc.schemas.object.ProofOfSpace;
import java.math.BigInteger;
import java.util.List;

public record SignagePointBundle(
        @JsonProperty("proofs") List<Proof> proofs,
        @JsonProperty("signage_point") SpInfo spInfo,
        @JsonProperty("success") boolean success
) {
    public SignagePointBundle { proofs = proofs == null ? List.of() : List.copyOf(proofs); }

    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    @JsonPropertyOrder({"plotIdentifier", "proofOfSpace"})
    public record Proof(String plotIdentifier, ProofOfSpace proofOfSpace) { }

    public record SpInfo(
            @JsonProperty("difficulty") @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger difficulty,
            @JsonProperty("signage_point_index") @JsonDeserialize(using = ExactIntegerDeserializer.class) int signagePointIndex,
            @JsonProperty("reward_chain_sp") String rewardChainSp,
            @JsonProperty("challenge_hash") String challengeHash,
            @JsonProperty("sub_slot_iters") @JsonDeserialize(using = ExactIntegerDeserializer.class) BigInteger subSlotIters,
            @JsonProperty("challenge_chain_sp") String challengeChainSp
    ) { }
}
