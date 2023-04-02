package io.mindspice.schemas.farmer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;


public record SignagePointBundle(
        // TODO: Not sure what proofs return if its and object or just a string, cached sps are returned
        //  so it needs check after a block hit or gotten from the chia code
        @JsonProperty("proofs") List<String> proofs,
        @JsonProperty("signage_point") SpInfo spInfo,
        @JsonProperty("success") boolean success
) {

    public SignagePointBundle {
        proofs = proofs != null ? Collections.unmodifiableList(proofs) : List.of();
    }


    record SpInfo(
            @JsonProperty("difficulty") int difficulty,
            @JsonProperty("signage_point_index") int signagePointIndex,
            @JsonProperty("reward_chain_sp") String rewardChainSp,
            @JsonProperty("challenge_hash") String challengeHash,
            @JsonProperty("sub_slot_iters") long subSlotIters,
            @JsonProperty("challenge_chain_sp") String challengeChainSp
    ) { }
}
