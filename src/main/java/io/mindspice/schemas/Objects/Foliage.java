package io.mindspice.schemas.Objects;


import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;


public record Foliage(
        FoliageBlockData foliage_block_data,
        String foliage_block_data_signature,
        String foliage_transaction_block_signature,
        String foliage_transaction_block_hash,
        String prev_block_hash,
        String reward_block_hash
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.FOLIAGE;
    }
}
