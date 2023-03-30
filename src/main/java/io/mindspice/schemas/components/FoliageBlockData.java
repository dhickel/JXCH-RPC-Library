package io.mindspice.schemas.components;


record FoliageBlockData(
        String extension_data,
        String farmer_reward_puzzle_hash,
        String pool_signature,
        PoolTarget pool_target,
        String unfinished_reward_block_hash
) { }
