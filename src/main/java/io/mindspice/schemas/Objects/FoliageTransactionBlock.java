package io.mindspice.schemas.Objects;

import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;


public record FoliageTransactionBlock(
        String prev_transaction_block_hash,
        long timestamp,
        String filter_hash,
        String additions_root,
        String removals_root,
        String transactions_info_hash
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.FOLIAGE_TRANSACTION_DATA;
    }
}
