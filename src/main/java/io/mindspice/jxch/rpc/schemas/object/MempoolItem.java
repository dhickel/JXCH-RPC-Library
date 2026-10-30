package io.mindspice.jxch.rpc.schemas.object;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.List;

public record MempoolItem(
        @JsonProperty("additions") List<Coin> additions,
        @JsonProperty("cost") BigInteger cost,
        @JsonProperty("fee") BigInteger fee,
        @JsonProperty("npc_result") NpcResult npcResult,
        @JsonProperty("removals") List<Coin> removals,
        @Deprecated @JsonInclude(JsonInclude.Include.NON_NULL) @JsonProperty("height_added_to_mempool") Long heightAddedToMempool,
        @JsonProperty("spend_bundle") SpendBundle spendBundle,
        @JsonProperty("spend_bundle_name") String spendBundleName
) {
    public MempoolItem {
        additions = additions == null ? List.of() : List.copyOf(additions);
        removals = removals == null ? List.of() : List.copyOf(removals);
    }

    public MempoolItem(List<Coin> additions, BigInteger cost, BigInteger fee, NpcResult npcResult,
                       List<Coin> removals, SpendBundle spendBundle, String spendBundleName) {
        this(additions, cost, fee, npcResult, removals, null, spendBundle, spendBundleName);
    }

    @Deprecated
    public MempoolItem(List<Coin> additions, long cost, long fee, NpcResult npcResult, List<Coin> removals,
                       int heightAddedToMempool, SpendBundle spendBundle, String spendBundleName) {
        this(additions, BigInteger.valueOf(cost), BigInteger.valueOf(fee), npcResult, removals,
                (long) heightAddedToMempool, spendBundle, spendBundleName);
    }
}
