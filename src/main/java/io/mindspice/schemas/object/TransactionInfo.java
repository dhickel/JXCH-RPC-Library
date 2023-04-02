package io.mindspice.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;


public record TransactionInfo(
        @JsonProperty("generator_root") String generatorRoot,
        @JsonProperty("generator_refs_root") String generatorRefsRoot,
        @JsonProperty("aggregated_signature") String aggregatedSignature,
        @JsonProperty("fees") String fees,
        @JsonProperty("cost") String cost,
        @JsonProperty("reward_claims_incorporated") List<Coin> rewardClaimsIncorporated
) {
    public TransactionInfo {
        rewardClaimsIncorporated = rewardClaimsIncorporated != null
                ? Collections.unmodifiableList(rewardClaimsIncorporated)
                : List.of();
    }
}
