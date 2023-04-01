package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;

import java.util.Collections;
import java.util.List;


public record MempoolItem(
        List<Coin> additions,
        long cost,
        long fee,
        NpcResult npc_result,
        List<Coin> removals,
        int height_added_to_mempool,
        SpendBundle spend_bundle,
        String spend_bundle_name
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.MEMPOOL_ITEM;
    }


    public MempoolItem {
        additions = additions == null ? List.of() : (additions);
        removals = removals == null? List.of() : Collections.unmodifiableList(removals);
    }
}
