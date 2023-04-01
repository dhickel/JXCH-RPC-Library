package io.mindspice.schemas.Objects;


import io.mindspice.enums.ObjectType;
import io.mindspice.schemas.BlockChainObject;


record FoliageBlockData(
        String extension_data,
        String farmer_reward_puzzle_hash,
        String pool_signature,
        PoolTarget pool_target,
        String unfinished_reward_block_hash
) implements BlockChainObject {
    @Override
    public ObjectType getObjectType() {
        return ObjectType.FOLIAGE_BLOCK_DATA;
    }
}
