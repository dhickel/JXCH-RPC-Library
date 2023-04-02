package io.mindspice.schemas.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;


public record MempoolItem(
        @JsonProperty("additions") List<Coin> additions,
        @JsonProperty("cost") long cost,
        @JsonProperty("fee") long fee,
        @JsonProperty("npc_result") NpcResult npcResult,
        @JsonProperty("removals") List<Coin> removals,
        @JsonProperty("height_added_to_mempool") int heightAddedToMempool,
        @JsonProperty("spend_bundle") SpendBundle spendBundle,
        @JsonProperty("spend_bundle_name") String spendBundleName
) {
    public MempoolItem {
        additions = additions != null ? Collections.unmodifiableList(additions) : List.of();
        removals = removals != null ? Collections.unmodifiableList(removals) : List.of();
    }
}