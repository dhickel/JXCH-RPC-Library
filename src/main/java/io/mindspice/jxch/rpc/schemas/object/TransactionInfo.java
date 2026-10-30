package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.List;

public record TransactionInfo(
        @JsonProperty("generator_root") String generatorRoot,
        @JsonProperty("generator_refs_root") String generatorRefsRoot,
        @JsonProperty("aggregated_signature") String aggregatedSignature,
        @JsonProperty("fees") BigInteger fees,
        @JsonProperty("cost") BigInteger cost,
        @JsonProperty("reward_claims_incorporated") List<Coin> rewardClaimsIncorporated
) {
    public TransactionInfo {
        rewardClaimsIncorporated = rewardClaimsIncorporated == null ? List.of() : List.copyOf(rewardClaimsIncorporated);
    }

    @Deprecated
    public TransactionInfo(String generatorRoot, String generatorRefsRoot, String aggregatedSignature,
                           Object fees, Object cost, List<Coin> rewardClaimsIncorporated) {
        this(generatorRoot, generatorRefsRoot, aggregatedSignature,
                fees == null ? null : new BigInteger(fees.toString()),
                cost == null ? null : new BigInteger(cost.toString()), rewardClaimsIncorporated);
    }
}
